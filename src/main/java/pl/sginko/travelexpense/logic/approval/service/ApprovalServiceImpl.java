package pl.sginko.travelexpense.logic.approval.service;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.actionLog.service.ActionLogService;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.logic.approval.event.ApprovalEvent;
import pl.sginko.travelexpense.logic.approval.exception.ApprovalException;
import pl.sginko.travelexpense.logic.approval.repository.ApprovalRepository;
import pl.sginko.travelexpense.logic.user.entity.Roles;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.exception.UserException;
import pl.sginko.travelexpense.logic.user.repository.UserRepository;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelStatus;
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ApprovalServiceImpl implements ApprovalService {
    private final TravelRepository travelRepository;
    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;
    private final TravelMapper travelMapper;
    private final ActionLogService actionLogService;
    private final ApplicationEventPublisher eventPublisher;
//    private final ApprovalProcessingService approvalProcessingService;
//    private final EmailService emailService;

    @Override
    public List<TravelResponseDto> getPendingApprovals(String approverEmail) {
        UserEntity approver = findApproverByEmail(approverEmail);
        Roles approverRole = approver.getRoles();

        List<TravelEntity> travels = getTravelsByStatuses(List.of(TravelStatus.SUBMITTED, TravelStatus.IN_PROCESS));
        List<TravelEntity> pendingTravels = filterPendingTravels(travels, approverRole);

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

    private List<TravelEntity> getTravelsByStatuses(List<TravelStatus> statuses) {
        return travelRepository.findByStatusIn(statuses);
    }

    private List<TravelEntity> filterPendingTravels(List<TravelEntity> travels, Roles approverRole) {
        return travels.stream()
                .filter(travel -> !approvalRepository.existsByTravelEntityAndRole(travel, approverRole))
                .collect(Collectors.toList());
    }

    private List<TravelResponseDto> mapTravelsToResponseDtos(List<TravelEntity> travels) {
        return travels.stream()
                .map(entity -> travelMapper.toResponseDto(entity))
                .collect(Collectors.toList());
    }

    @Transactional
    private void processApproval(UUID travelId, String approverEmail, ApprovalStatus newStatus) {
        UserEntity approver = findApproverByEmail(approverEmail);
        Roles approverRole = approver.getRoles();
        TravelEntity travelEntity = findTravelByTechId(travelId);

        validateApprovalStatus(travelEntity, approverRole);

        ApprovalEntity approval = new ApprovalEntity(travelEntity, approver, approverRole);
        approval.validateApprovalStatus();
        approval.updateStatus(newStatus);

        approvalRepository.save(approval);
        travelEntity.updateStatusBasedOnApprovals();

        actionLogService.logAction("Status report: " + travelEntity.getTechId() + " updated to: "
                + newStatus, travelEntity.getId(), approver.getId());

//        notifyUserIfStatusChanged(travelEntity);
        publishApprovalEvent(travelEntity);
    }

    private void publishApprovalEvent(TravelEntity travelEntity) {
        TravelStatus travelStatus = travelEntity.getStatus();
        if (travelStatus == TravelStatus.APPROVED || travelStatus == TravelStatus.REJECTED) {
            ApprovalEvent event = new ApprovalEvent(travelEntity.getTechId(), travelEntity.getUserEntity().getEmail(), travelStatus);
            eventPublisher.publishEvent(event);
        }
    }

//    private void notifyUserIfStatusChanged(TravelEntity travelEntity) {
//        String userEmail = travelEntity.getUserEntity().getEmail();
//        if (travelEntity.getStatus() == TravelStatus.APPROVED || travelEntity.getStatus() == TravelStatus.REJECTED) {
//            emailService.sendApprovalNotification(userEmail, travelEntity.getTechId(), travelEntity.getStatus());
//        }
//    }

    private UserEntity findApproverByEmail(String approverEmail) {
        return userRepository.findByEmail(approverEmail)
                .orElseThrow(() -> new UserException("Cannot find user with email: " + approverEmail));
    }

    private TravelEntity findTravelByTechId(UUID techId) {
        return travelRepository.findByTechId(techId)
                .orElseThrow(() -> new TravelException("Travel not found"));
    }

    private void validateApprovalStatus(TravelEntity travelEntity, Roles role) {
        boolean alreadyApproved = approvalRepository.existsByTravelEntityAndRole(travelEntity, role);
        if (alreadyApproved) {
            throw new ApprovalException("Approval has already been processed by a " + role);
        }
    }
}
