/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.domain.approval.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.common.eventPublisher.EventPublisher;
import pl.sginko.travelexpense.domain.actionLog.service.ActionLogService;
import pl.sginko.travelexpense.domain.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.domain.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.domain.approval.exception.ApprovalException;
import pl.sginko.travelexpense.domain.approval.repository.ApprovalRepository;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.travelReport.mapper.TravelReportMapper;
import pl.sginko.travelexpense.domain.travelReport.repository.TravelReportRepository;
import pl.sginko.travelexpense.domain.user.entity.Roles;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.exception.UserException;
import pl.sginko.travelexpense.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Set;
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
    private final EventPublisher eventPublisher;

    @Override
    public List<TravelReportResponseDto> getTravelReportsForApproval(String approverEmail) {
        // Find the approver by their email address
        UserEntity approver = findApproverUserByEmail(approverEmail);

        // Get the role of the approver (e.g., ROLE_MANAGER, ROLE_ACCOUNTANT)
        Roles approverRole = approver.getRoles();

        // Retrieve all travel reports with statuses SUBMITTED or IN_PROCESS
        List<TravelReportEntity> travelReportEntity = getTravelReportsByStatuses(List.of(TravelReportStatus.SUBMITTED, TravelReportStatus.IN_PROCESS));

        // Filter the travel reports to keep only those that haven't been approved by the approver's role
        List<TravelReportEntity> pendingTravelReportEntity = filterPendingTravelReports(travelReportEntity, approverRole);

        // Map the filtered travelReportEntity to a list of TravelReportResponseDto for the client
        return mapTravelReportEntityToTravelReportResponseDto(pendingTravelReportEntity);
    }

    @Transactional
    @Override
    public void approveTravelReport(UUID travelId, String approverEmail) {
        processTravelReportApproval(travelId, approverEmail, ApprovalStatus.APPROVED);
    }

    @Transactional
    @Override
    public void rejectTravelReport(UUID travelId, String approverEmail) {
        processTravelReportApproval(travelId, approverEmail, ApprovalStatus.REJECTED);
    }

    private List<TravelReportEntity> getTravelReportsByStatuses(List<TravelReportStatus> travelReportStatus) {
        return travelReportRepository.findByStatusIn(travelReportStatus);
    }

    private List<TravelReportEntity> filterPendingTravelReports(List<TravelReportEntity> travelReports, Roles approverRole) {
        return travelReports.stream()
                .filter(travel -> !approvalRepository.existsByTravelReportEntityAndRole(travel, approverRole))
                .collect(Collectors.toList());
    }

    private List<TravelReportResponseDto> mapTravelReportEntityToTravelReportResponseDto(List<TravelReportEntity> travels) {
        return travels.stream()
                .map(entity -> travelReportMapper.toResponseDto(entity))
                .collect(Collectors.toList());
    }

    private void processTravelReportApproval(UUID travelId, String approverEmail, ApprovalStatus newStatus) {
        // Find the approver by their email address
        UserEntity approver = findApproverUserByEmail(approverEmail);

        // Get the role of the approver (e.g., ROLE_MANAGER, ROLE_ACCOUNTANT)
        Roles approverRole = approver.getRoles();

        // Find the travelReport by its unique techId (UUID)
        TravelReportEntity travelReportEntity = findTravelByTechId(travelId);

        // Validate that the travelReport has not already been approved/rejected by this role
        checkIfApprovalAlreadyExists(travelReportEntity, approverRole);

        addApprovalToTravelReport(newStatus, travelReportEntity, approver, approverRole);

        updateTravelReportStatus(travelReportEntity);

        logAndPublishEvent(newStatus, travelReportEntity, approver);
    }

    private void addApprovalToTravelReport(ApprovalStatus newStatus, TravelReportEntity travelReportEntity, UserEntity approver, Roles approverRole) {
        // Create a new approval record (ApprovalEntity) for the travelReport and the approver
        ApprovalEntity approval = new ApprovalEntity(travelReportEntity, approver, approverRole);

        // Validate the approval status
        approval.checkIfStatusPending();

        // Update the approval status to the new value (APPROVED or REJECTED)
        approval.updateApprovalStatus(newStatus);

        // Save the updated approval record to the database
        approvalRepository.save(approval);

        // Add the approval to the travel report's Set of approvals
        Set<ApprovalEntity> approvals = travelReportEntity.getApprovals();
        approvals.add(approval);
    }

    private void updateTravelReportStatus(TravelReportEntity travelReportEntity) {
        travelReportEntity.updateTravelReportStatusFromApprovals();

        travelReportRepository.save(travelReportEntity);
    }

    private void logAndPublishEvent(ApprovalStatus newStatus, TravelReportEntity travelReportEntity, UserEntity approver) {
        // Log the action performed by the approver (e.g., approval or rejection)
        actionLogService.logAction("Status report: " + travelReportEntity.getTechId() + " updated to: "
                + newStatus, travelReportEntity.getId(), approver.getId());

        // Publish an event if the travel report's status is now APPROVED or REJECTED
        eventPublisher.publishTravelReportApprovalEvent(
                travelReportEntity.getTechId(),
                travelReportEntity.getUserEntity().getEmail(),
                travelReportEntity.getStatus());
    }

    private UserEntity findApproverUserByEmail(String approverEmail) {
        return userRepository.findByEmail(approverEmail)
                .orElseThrow(() -> new UserException("Cannot find user with email: " + approverEmail));
    }

    private TravelReportEntity findTravelByTechId(UUID techId) {
        return travelReportRepository.findByTechId(techId)
                .orElseThrow(() -> new TravelReportException("Travel not found"));
    }

    private void checkIfApprovalAlreadyExists(TravelReportEntity travelReportEntity, Roles role) {
        boolean alreadyApproved = approvalRepository.existsByTravelReportEntityAndRole(travelReportEntity, role);
        if (alreadyApproved) {
            throw new ApprovalException("Approval has already been processed by a " + role);
        }
    }
}
