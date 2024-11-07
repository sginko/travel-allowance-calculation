package pl.sginko.travelexpense.logic.approval.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.exception.UserException;
import pl.sginko.travelexpense.logic.auth.repository.UserRepository;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.logic.approval.exception.ApprovalException;
import pl.sginko.travelexpense.logic.approval.repository.ApprovalRepository;
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

        List<ApprovalEntity> pendingApprovals = approvalRepository.findByApproverAndStatus(approver, ApprovalStatus.PENDING);

        return pendingApprovals.stream()
                .map(approval -> travelMapper.toResponseDto(approval.getTravelEntity()))
                .collect(Collectors.toList());
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

    private void processApproval(UUID travelId, String approverEmail, ApprovalStatus newStatus) {
        UserEntity approver = userRepository.findByEmail(approverEmail)
                .orElseThrow(() -> new UserException("Cannot find user with email: " + approverEmail));

        TravelEntity travelEntity = travelRepository.findByTechId(travelId)
                .orElseThrow(() -> new TravelException("Travel not found"));

        ApprovalEntity approval = approvalRepository.findByTravelEntityAndApprover(travelEntity, approver)
                .orElseThrow(() -> new ApprovalException("Approval not found for approver: " + approverEmail));

        if (approval.getStatus() != ApprovalStatus.PENDING) {
            throw new ApprovalException("Approval already processed for approver: " + approverEmail);
        }

        approval.updateStatus(newStatus);

        if (newStatus == ApprovalStatus.REJECTED) {
            travelEntity.changeStatus(TravelStatus.REJECTED);
            // emailService.sendRejectionEmail(travelEntity.getUserEntity().getEmail(), travelEntity.getTechId());
        } else if (allApprovalsApproved(travelEntity)) {
            travelEntity.changeStatus(TravelStatus.APPROVED);
            // emailService.sendApprovalEmail(travelEntity.getUserEntity().getEmail(), travelEntity.getTechId());
        } else if (allApprovalsCompleted(travelEntity)) {
            travelEntity.changeStatus(TravelStatus.REJECTED);
            // emailService.sendRejectionEmail(travelEntity.getUserEntity().getEmail(), travelEntity.getTechId());
        } else {
            travelEntity.changeStatus(TravelStatus.IN_PROCESS);
        }

        travelRepository.save(travelEntity);
    }

    private boolean allApprovalsCompleted(TravelEntity travelEntity) {
        return travelEntity.getApprovals().stream()
                .allMatch(approval -> approval.getStatus() != ApprovalStatus.PENDING);
    }

    private boolean allApprovalsApproved(TravelEntity travelEntity) {
        return travelEntity.getApprovals().stream()
                .allMatch(approval -> approval.getStatus() == ApprovalStatus.APPROVED);
    }
}
