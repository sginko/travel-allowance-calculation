package pl.sginko.travelexpense.domain.approval.service;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.domain.actionLog.service.ActionLogService;
import pl.sginko.travelexpense.domain.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.domain.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.domain.approval.event.ApprovalEvent;
import pl.sginko.travelexpense.domain.approval.exception.ApprovalException;
import pl.sginko.travelexpense.domain.approval.repository.ApprovalRepository;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.travelReport.mapper.TravelReportMapper;
import pl.sginko.travelexpense.domain.travelReport.repository.TravelReportRepository;
import pl.sginko.travelexpense.domain.user.entity.UserRoles;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.exception.UserException;
import pl.sginko.travelexpense.domain.user.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ApprovalServiceImpl implements ApprovalService {
    private final TravelReportRepository travelReportRepository;
    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;
    private final TravelReportMapper travelReportMapper;
    private final ActionLogService actionLogService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<TravelReportResponseDto> getPendingApprovals(String approverEmail) {
        UserEntity approver = findApproverByEmail(approverEmail);
        UserRoles approverRole = approver.getUserRoles();

        List<TravelReportEntity> travels = getTravelsByStatuses(List.of(TravelReportStatus.SUBMITTED, TravelReportStatus.IN_PROCESS));
        List<TravelReportEntity> pendingTravels = filterPendingTravels(travels, approverRole);

        return mapTravelsToResponseDtos(pendingTravels);
    }

    @Transactional
    @Override
    public void approveTravel(UUID travelId, String approverEmail) {
        processApproval(travelId, approverEmail, ApprovalStatus.APPROVED);
    }

    @Transactional
    @Override
    public void rejectTravel(UUID travelId, String approverEmail) {
        processApproval(travelId, approverEmail, ApprovalStatus.REJECTED);
    }

    private List<TravelReportEntity> getTravelsByStatuses(List<TravelReportStatus> statuses) {
        return travelReportRepository.findByStatusIn(statuses);
    }

    private List<TravelReportEntity> filterPendingTravels(List<TravelReportEntity> travels, UserRoles approverRole) {
        return travels.stream()
                .filter(travel -> !approvalRepository.existsByTravelReportEntityAndRole(travel, approverRole))
                .collect(Collectors.toList());
    }

    private List<TravelReportResponseDto> mapTravelsToResponseDtos(List<TravelReportEntity> travels) {
        return travels.stream()
                .map(entity -> travelReportMapper.toResponseDto(entity))
                .collect(Collectors.toList());
    }

    private void processApproval(UUID travelId, String approverEmail, ApprovalStatus newStatus) {
        UserEntity approver = findApproverByEmail(approverEmail);
        UserRoles approverRole = approver.getUserRoles();
        TravelReportEntity travelReportEntity = findTravelByTechId(travelId);

        validateApprovalStatus(travelReportEntity, approverRole);

        ApprovalEntity approval = new ApprovalEntity(travelReportEntity, approver, approverRole);
        approval.validateApprovalStatus();
        approval.updateStatus(newStatus);

        approvalRepository.save(approval);

        travelReportEntity.getApprovals().add(approval);

        travelReportEntity.updateTravelReportStatusFromApprovals();

        travelReportRepository.save(travelReportEntity);

        actionLogService.logAction("Status report: " + travelReportEntity.getTechId() + " updated to: "
                + newStatus, travelReportEntity.getId(), approver.getId());

        publishApprovalEvent(travelReportEntity);
    }

    private void publishApprovalEvent(TravelReportEntity travelReportEntity) {
        TravelReportStatus travelReportStatus = travelReportEntity.getStatus();
        if (travelReportStatus == TravelReportStatus.APPROVED || travelReportStatus == TravelReportStatus.REJECTED) {
            ApprovalEvent event = new ApprovalEvent(travelReportEntity.getTechId(), travelReportEntity.getUserEntity().getEmail(),
                    travelReportStatus);
            eventPublisher.publishEvent(event);
        }
    }

    private UserEntity findApproverByEmail(String approverEmail) {
        return userRepository.findByEmail(approverEmail)
                .orElseThrow(() -> new UserException("Cannot find user with email: " + approverEmail));
    }

    private TravelReportEntity findTravelByTechId(UUID techId) {
        return travelReportRepository.findByTechId(techId)
                .orElseThrow(() -> new TravelReportException("Travel not found"));
    }

    private void validateApprovalStatus(TravelReportEntity travelReportEntity, UserRoles role) {
        boolean alreadyApproved = approvalRepository.existsByTravelReportEntityAndRole(travelReportEntity, role);
        if (alreadyApproved) {
            throw new ApprovalException("Approval has already been processed by a " + role);
        }
    }
}
