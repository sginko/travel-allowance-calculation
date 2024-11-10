package pl.sginko.travelexpense.domen.approval.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.domen.actionLog.ActionLogService;
import pl.sginko.travelexpense.domen.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.domen.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.domen.approval.exception.ApprovalException;
import pl.sginko.travelexpense.domen.approval.repository.ApprovalRepository;
import pl.sginko.travelexpense.domen.auth.entity.Roles;
import pl.sginko.travelexpense.domen.auth.entity.UserEntity;
import pl.sginko.travelexpense.domen.auth.exception.UserException;
import pl.sginko.travelexpense.domen.auth.repository.UserRepository;
import pl.sginko.travelexpense.domen.emailService.EmailService;
import pl.sginko.travelexpense.domen.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelStatus;
import pl.sginko.travelexpense.domen.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.domen.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.domen.travelexpense.travel.repository.TravelRepository;

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
    private final EmailService emailService;

    @Transactional
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

        actionLogService.logAction("Status report: "+ travelEntity.getTechId() + " updated to: " + newStatus, travelEntity.getId(), approver.getId());

        notifyUserIfStatusChanged(travelEntity);
    }

    private void notifyUserIfStatusChanged(TravelEntity travelEntity) {
        String userEmail = travelEntity.getUserEntity().getEmail();
        if (travelEntity.getStatus() == TravelStatus.APPROVED || travelEntity.getStatus() == TravelStatus.REJECTED) {
            emailService.sendApprovalNotification(userEmail, travelEntity.getTechId(), travelEntity.getStatus());
        }
    }

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
