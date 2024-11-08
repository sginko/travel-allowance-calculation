package pl.sginko.travelexpense.logic.approval.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.logic.approval.exception.ApprovalException;
import pl.sginko.travelexpense.logic.approval.repository.ApprovalRepository;
import pl.sginko.travelexpense.logic.auth.entity.Roles;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.exception.UserException;
import pl.sginko.travelexpense.logic.auth.repository.UserRepository;
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
//    private final EmailService emailService;

    @Transactional
    @Override
    public List<TravelResponseDto> getPendingApprovals(String approverEmail) {
        UserEntity approver = userRepository.findByEmail(approverEmail)
                .orElseThrow(() -> new UserException("Cannot find user with email: " + approverEmail));

        List<ApprovalEntity> pendingApprovals = approvalRepository.findByApproverAndStatus(approver, ApprovalStatus.PENDING)
                .stream()
                .filter(approval -> {
                    TravelEntity travel = approval.getTravelEntity();
                    return travel.getStatus() != TravelStatus.APPROVED && travel.getStatus() != TravelStatus.REJECTED;
                })
                .collect(Collectors.toList());

        return pendingApprovals.stream()
                .map(approval -> travelMapper.toResponseDto(approval.getTravelEntity()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void approveTravel(UUID travelId, String approverEmail) {
        processApproval(travelId, approverEmail, ApprovalStatus.APPROVED);
    }

    //    @Retryable(value = OptimisticLockException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Transactional
    @Override
    public void rejectTravel(UUID travelId, String approverEmail) {
        processApproval(travelId, approverEmail, ApprovalStatus.REJECTED);
    }

    private void processApproval(UUID travelId, String approverEmail, ApprovalStatus newStatus) {
        try {
            UserEntity approver = userRepository.findByEmail(approverEmail)
                    .orElseThrow(() -> new UserException("Cannot find user with email: " + approverEmail));

            TravelEntity travelEntity = travelRepository.findByTechId(travelId)
                    .orElseThrow(() -> new TravelException("Travel not found"));

            travelEntity.validateStatusForApproval();

            ApprovalEntity approval = approvalRepository.findByTravelEntityAndApprover(travelEntity, approver)
                    .orElseThrow(() -> new ApprovalException("Approval not found for approver: " + approverEmail));

            approval.validatePendingStatus(approverEmail);

            approval.updateStatus(newStatus);

//            deletePendingApprovalsForTravel(travelEntity);

//            deletePendingApprovalsForRole(travelEntity, approver.getRoles());

            travelEntity.updateStatusBasedOnApprovals();

            if (travelEntity.getStatus() == TravelStatus.APPROVED || travelEntity.getStatus() == TravelStatus.REJECTED) {
                deletePendingApprovalsForTravel(travelEntity);
            }

        } catch (OptimisticLockException e) {
            throw new ApprovalException("Data has been modified by another user. Please refresh the page and try again.");
        }
    }

    private void deletePendingApprovalsForTravel(TravelEntity travelEntity) {
        List<ApprovalEntity> pendingApprovals = approvalRepository.findByTravelEntityAndStatus(travelEntity, ApprovalStatus.PENDING);
        approvalRepository.deleteAll(pendingApprovals);
    }

    private void validateTravelStatus(TravelEntity travelEntity) {
        if (travelEntity.getStatus() == TravelStatus.APPROVED || travelEntity.getStatus() == TravelStatus.REJECTED) {
            throw new ApprovalException("The travel report has already been finalized and cannot be modified.");
        }
    }

    private void validateApprovalStatus(ApprovalEntity approval, String approverEmail) {
        if (approval.getStatus() != ApprovalStatus.PENDING) {
            throw new ApprovalException("Approval already processed for approver: " + approverEmail);
        }
    }

//    private void deletePendingApprovalsForRole(TravelEntity travelEntity, Roles role) {
//        List<ApprovalEntity> pendingApprovals = approvalRepository.findByTravelEntityAndStatus(travelEntity, ApprovalStatus.PENDING)
//                .stream()
//                .filter(approval -> approval.getRole().equals(role))
//                .collect(Collectors.toList());
//        approvalRepository.deleteAll(pendingApprovals);
//    }

//    private void deletePendingApprovalsForTravel(TravelEntity travelEntity) {
//        if (travelEntity.getStatus() == TravelStatus.APPROVED || travelEntity.getStatus() == TravelStatus.REJECTED) {
//            List<ApprovalEntity> pendingApprovals = approvalRepository.findByTravelEntityAndStatus(travelEntity, ApprovalStatus.PENDING);
//            approvalRepository.deleteAll(pendingApprovals);
//        }

//        if (travelEntity.getStatus() == TravelStatus.APPROVED || travelEntity.getStatus() == TravelStatus.REJECTED) {
//            List<ApprovalEntity> pendingApprovals = approvalRepository.findByTravelEntity(travelEntity)
//                    .stream()
//                    .filter(approval -> approval.getStatus() == ApprovalStatus.PENDING)
//                    .collect(Collectors.toList());
//            approvalRepository.deleteAll(pendingApprovals);
//        }
//    }
}
