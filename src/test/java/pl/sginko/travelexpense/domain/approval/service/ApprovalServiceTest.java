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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalServiceTest {
    @Mock
    private TravelReportRepository travelReportRepository;

    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActionLogService actionLogService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TravelReportMapper travelReportMapper;

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    private UserEntity approver;
    private TravelReportEntity travelReportEntity;
    private UUID travelId;

    @BeforeEach
    void setUp() {
        approver = new UserEntity("approver@test.com", "Approver", "User", "password");
        approver.changeRoleToManager();

        travelReportEntity = new TravelReportEntity("CityA", "CityB", LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0), approver, BigDecimal.ZERO, BigDecimal.ZERO);
        travelReportEntity.updateStatus(TravelReportStatus.SUBMITTED);

        travelId = travelReportEntity.getTechId();

        UserEntity accountant = new UserEntity("accountant@test.com", "Accountant", "User", "password");
        accountant.changeRoleToAccountant();

        ApprovalEntity existingApproval = new ApprovalEntity(travelReportEntity, accountant, accountant.getRoles());
        existingApproval.updateApprovalStatus(ApprovalStatus.APPROVED);
        travelReportEntity.getApprovals().add(existingApproval);
    }

    @Test
    void should_approve_travel_successfully() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelReportRepository.findByTechId(travelId)).thenReturn(Optional.of(travelReportEntity));
        when(approvalRepository.existsByTravelReportEntityAndRole(travelReportEntity, approver.getRoles())).thenReturn(false);

        // WHEN
        approvalService.approveTravelReport(travelId, approver.getEmail());

        // THEN
        ArgumentCaptor<ApprovalEntity> approvalCaptor = ArgumentCaptor.forClass(ApprovalEntity.class);
        verify(approvalRepository).save(approvalCaptor.capture());
        ApprovalEntity savedApproval = approvalCaptor.getValue();

        assertThat(savedApproval.getTravelReportEntity()).isEqualTo(travelReportEntity);
        assertThat(savedApproval.getApprover()).isEqualTo(approver);
        assertThat(savedApproval.getRole()).isEqualTo(approver.getRoles());
        assertThat(savedApproval.getStatus()).isEqualTo(ApprovalStatus.APPROVED);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> travelIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> approverIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(actionLogService).logAction(messageCaptor.capture(), travelIdCaptor.capture(), approverIdCaptor.capture());

        assertThat(messageCaptor.getValue()).isEqualTo("Status report: " + travelReportEntity.getTechId() + " updated to: " + ApprovalStatus.APPROVED);
        assertThat(travelIdCaptor.getValue()).isEqualTo(travelReportEntity.getId());
        assertThat(approverIdCaptor.getValue()).isEqualTo(approver.getId());

        ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TravelReportStatus> statusCaptor = ArgumentCaptor.forClass(TravelReportStatus.class);
        verify(eventPublisher).publishTravelReportApprovalEvent(uuidCaptor.capture(), emailCaptor.capture(), statusCaptor.capture());

        assertThat(uuidCaptor.getValue()).isEqualTo(travelId);
        assertThat(emailCaptor.getValue()).isEqualTo(travelReportEntity.getUserEntity().getEmail());
        assertThat(statusCaptor.getValue()).isEqualTo(TravelReportStatus.APPROVED);

        assertThat(travelReportEntity.getStatus()).isEqualTo(TravelReportStatus.APPROVED);
    }

    @Test
    void should_reject_travel_successfully() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelReportRepository.findByTechId(travelId)).thenReturn(Optional.of(travelReportEntity));
        when(approvalRepository.existsByTravelReportEntityAndRole(travelReportEntity, approver.getRoles())).thenReturn(false);

        // WHEN
        approvalService.rejectTravelReport(travelId, approver.getEmail());

        // THEN
        ArgumentCaptor<ApprovalEntity> approvalCaptor = ArgumentCaptor.forClass(ApprovalEntity.class);
        verify(approvalRepository).save(approvalCaptor.capture());
        ApprovalEntity savedApproval = approvalCaptor.getValue();

        assertThat(savedApproval.getTravelReportEntity()).isEqualTo(travelReportEntity);
        assertThat(savedApproval.getApprover()).isEqualTo(approver);
        assertThat(savedApproval.getRole()).isEqualTo(approver.getRoles());
        assertThat(savedApproval.getStatus()).isEqualTo(ApprovalStatus.REJECTED);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> travelIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> approverIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(actionLogService).logAction(messageCaptor.capture(), travelIdCaptor.capture(), approverIdCaptor.capture());

        assertThat(messageCaptor.getValue()).isEqualTo("Status report: " + travelReportEntity.getTechId() + " updated to: " + ApprovalStatus.REJECTED);
        assertThat(travelIdCaptor.getValue()).isEqualTo(travelReportEntity.getId());
        assertThat(approverIdCaptor.getValue()).isEqualTo(approver.getId());

        // Захват аргументов метода publishTravelReportApprovalEvent
        ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TravelReportStatus> statusCaptor = ArgumentCaptor.forClass(TravelReportStatus.class);
        verify(eventPublisher).publishTravelReportApprovalEvent(uuidCaptor.capture(), emailCaptor.capture(), statusCaptor.capture());

        // Проверка захваченных значений
        assertThat(uuidCaptor.getValue()).isEqualTo(travelId);
        assertThat(emailCaptor.getValue()).isEqualTo(travelReportEntity.getUserEntity().getEmail());
        assertThat(statusCaptor.getValue()).isEqualTo(TravelReportStatus.REJECTED);

        // Проверка обновленного статуса
        assertThat(travelReportEntity.getStatus()).isEqualTo(TravelReportStatus.REJECTED);
    }

    @Test
    void should_publish_event_when_status_is_approved() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelReportRepository.findByTechId(travelId)).thenReturn(Optional.of(travelReportEntity));
        when(approvalRepository.existsByTravelReportEntityAndRole(travelReportEntity, approver.getRoles())).thenReturn(false);

        // WHEN
        approvalService.approveTravelReport(travelId, approver.getEmail());

        // THEN
        verify(eventPublisher).publishTravelReportApprovalEvent(eq(travelId), eq(approver.getEmail()), eq(TravelReportStatus.APPROVED));
        assertThat(travelReportEntity.getStatus()).isEqualTo(TravelReportStatus.APPROVED);
    }

    @Test
    void should_publish_event_when_status_is_rejected() {
        // GIVEN
        TravelReportEntity newTravelReport = new TravelReportEntity("CityX", "CityY", LocalDate.now(), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(19, 0), approver, BigDecimal.ZERO, BigDecimal.ZERO);
        newTravelReport.updateStatus(TravelReportStatus.SUBMITTED);

        UserEntity accountant2 = new UserEntity("accountant2@test.com", "Accountant2", "User2", "password");
        accountant2.changeRoleToAccountant();
        ApprovalEntity existingApproval2 = new ApprovalEntity(newTravelReport, accountant2, accountant2.getRoles());
        existingApproval2.updateApprovalStatus(ApprovalStatus.APPROVED);
        newTravelReport.getApprovals().add(existingApproval2);

        UUID newTravelId = newTravelReport.getTechId();

        when(travelReportRepository.findByTechId(newTravelId)).thenReturn(Optional.of(newTravelReport));
        when(approvalRepository.existsByTravelReportEntityAndRole(newTravelReport, approver.getRoles())).thenReturn(false);
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));

        // WHEN
        approvalService.rejectTravelReport(newTravelId, approver.getEmail());

        // THEN
        verify(eventPublisher).publishTravelReportApprovalEvent(eq(newTravelId), eq(approver.getEmail()), eq(TravelReportStatus.REJECTED));
        assertThat(newTravelReport.getStatus()).isEqualTo(TravelReportStatus.REJECTED);
    }

    @Test
    void should_throw_exception_when_rejecting_already_processed_travel() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelReportRepository.findByTechId(travelId)).thenReturn(Optional.of(travelReportEntity));
        when(approvalRepository.existsByTravelReportEntityAndRole(eq(travelReportEntity), eq(approver.getRoles()))).thenReturn(true);

        // WHEN
        Executable e = () -> approvalService.rejectTravelReport(travelId, approver.getEmail());

        // THEN
        ApprovalException exception = assertThrows(ApprovalException.class, e);
        assertThat(exception.getMessage()).isEqualTo("Approval has already been processed by a " + approver.getRoles());
        verify(approvalRepository, never()).save(any(ApprovalEntity.class));
    }

    @Test
    void should_throw_exception_when_rejecting_nonexistent_travel() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelReportRepository.findByTechId(travelId)).thenReturn(Optional.empty());

        // WHEN
        Executable e = () -> approvalService.rejectTravelReport(travelId, approver.getEmail());

        // THEN
        TravelReportException exception = assertThrows(TravelReportException.class, e);
        assertThat(exception.getMessage()).isEqualTo("Travel not found");
        verify(approvalRepository, never()).save(any(ApprovalEntity.class));
    }

    @Test
    void should_get_pending_approvals_successfully() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));

        UserEntity user1 = new UserEntity("user1@test.com", "user1", "surname1", "password");
        UserEntity user2 = new UserEntity("user2@test.com", "user2", "surname2", "password");

        TravelReportEntity travel1 = createTestTravelReport("CityA", "CityB", TravelReportStatus.SUBMITTED, user1);
        TravelReportEntity travel2 = createTestTravelReport("CityC", "CityD", TravelReportStatus.IN_PROCESS, user2);

        List<TravelReportEntity> allTravels = List.of(travel1, travel2);

        when(travelReportRepository.findByStatusIn(anyList())).thenReturn(allTravels);
        when(approvalRepository.existsByTravelReportEntityAndRole(eq(travel1), eq(approver.getRoles()))).thenReturn(false);
        when(approvalRepository.existsByTravelReportEntityAndRole(eq(travel2), eq(approver.getRoles()))).thenReturn(false);

        TravelReportResponseDto responseDto1 = createTestTravelReportResponseDto(travel1);
        TravelReportResponseDto responseDto2 = createTestTravelReportResponseDto(travel2);

        when(travelReportMapper.toResponseDto(travel1)).thenReturn(responseDto1);
        when(travelReportMapper.toResponseDto(travel2)).thenReturn(responseDto2);

        // WHEN
        List<TravelReportResponseDto> pendingApprovals = approvalService.getTravelReportsForApproval(approver.getEmail());

        // THEN
        assertThat(pendingApprovals).hasSize(2);
        assertThat(pendingApprovals)
                .extracting(TravelReportResponseDto::getFromCity)
                .containsExactlyInAnyOrder("CityA", "CityC");
    }

    @Test
    void should_not_return_already_approved_travels_in_pending_approvals() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));

        UserEntity user1 = new UserEntity("user1@example.com", "User1", "Surname1", "password");
        UserEntity user2 = new UserEntity("user2@example.com", "User2", "Surname2", "password");

        TravelReportEntity travel1 = createTestTravelReport("CityA", "CityB", TravelReportStatus.SUBMITTED, user1);
        TravelReportEntity travel2 = createTestTravelReport("CityC", "CityD", TravelReportStatus.IN_PROCESS, user2);

        List<TravelReportEntity> allTravels = List.of(travel1, travel2);

        when(travelReportRepository.findByStatusIn(anyList())).thenReturn(allTravels);
        when(approvalRepository.existsByTravelReportEntityAndRole(eq(travel1), eq(approver.getRoles()))).thenReturn(true);
        when(approvalRepository.existsByTravelReportEntityAndRole(eq(travel2), eq(approver.getRoles()))).thenReturn(false);

        TravelReportResponseDto responseDto2 = createTestTravelReportResponseDto(travel2);
        when(travelReportMapper.toResponseDto(travel2)).thenReturn(responseDto2);

        // WHEN
        List<TravelReportResponseDto> pendingApprovals = approvalService.getTravelReportsForApproval(approver.getEmail());

        // THEN
        assertThat(pendingApprovals).hasSize(1);
        assertThat(pendingApprovals.get(0).getFromCity()).isEqualTo("CityC");
    }

    @Test
    void should_return_empty_list_when_no_pending_approvals() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelReportRepository.findByStatusIn(anyList())).thenReturn(List.of());

        // WHEN
        List<TravelReportResponseDto> pendingApprovals = approvalService.getTravelReportsForApproval(approver.getEmail());

        // THEN
        assertThat(pendingApprovals).isEmpty();
    }

    private TravelReportEntity createTestTravelReport(String fromCity, String toCity, TravelReportStatus status, UserEntity user) {
        TravelReportEntity travel = new TravelReportEntity(fromCity, toCity, LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0), user, BigDecimal.ZERO, BigDecimal.ZERO);

        travel.updateStatus(status);

        return travel;
    }

    private TravelReportResponseDto createTestTravelReportResponseDto(TravelReportEntity travelEntity) {
        return new TravelReportResponseDto(
                travelEntity.getTechId(),
                travelEntity.getUserEntity().getEmail(),
                travelEntity.getFromCity(),
                travelEntity.getToCity(),
                travelEntity.getStartDate(),
                travelEntity.getStartTime(),
                travelEntity.getEndDate(),
                travelEntity.getEndTime(),
                travelEntity.getOtherExpenses(),
                travelEntity.getTotalAmount(),
                travelEntity.getAdvancePayment(),
                null,
                null,
                null);
    }
}
