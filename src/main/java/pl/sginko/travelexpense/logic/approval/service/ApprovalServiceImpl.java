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

import java.util.Arrays;
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

        Roles approverRole = approver.getRoles();

        List<TravelEntity> travels = travelRepository.findByStatusIn(
                List.of(TravelStatus.SUBMITTED, TravelStatus.IN_PROCESS));

        List<TravelEntity> pendingTravels = travels.stream()
                .filter(travel -> !approvalRepository.existsByTravelEntityAndRole(travel, approverRole))
                .collect(Collectors.toList());

        return pendingTravels.stream()
                .map(travelMapper::toResponseDto)
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

        Roles approverRole  = approver.getRoles();

        boolean alreadyApproved = approvalRepository.existsByTravelEntityAndRole(travelEntity, approverRole);
        if (alreadyApproved) {
            throw new ApprovalException("Approval has already been processed by a " + approverRole);
        }

        ApprovalEntity approval = new ApprovalEntity(travelEntity, approver, approver.getRoles());

        approval.validateApprovalStatus();

        approval.updateStatus(newStatus);

        approvalRepository.save(approval);

        travelEntity.updateStatusBasedOnApprovals();
    }
}
