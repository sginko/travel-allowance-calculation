package pl.sginko.travelexpense.logic.approval.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.sginko.travelexpense.logic.actionLog.service.ActionLogService;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.approval.event.ApprovalEvent;
import pl.sginko.travelexpense.logic.approval.exception.ApprovalException;
import pl.sginko.travelexpense.logic.approval.repository.ApprovalRepository;
import pl.sginko.travelexpense.logic.user.entity.Roles;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.repository.UserRepository;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelStatus;
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalServiceTest {

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActionLogService actionLogService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    private UserEntity approver;
    private TravelEntity travelEntity;
    private ApprovalEntity approvalEntity;

    @BeforeEach
    void setUp() {
        approver = new UserEntity("approver@example.com", "Approver", "User", "password");
        approver.changeRoleToManager();

        travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                approver, BigDecimal.ZERO, BigDecimal.ZERO);

        approvalEntity = new ApprovalEntity(travelEntity, approver, Roles.ROLE_MANAGER);
    }

    @Test
    void should_get_pending_approvals() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelRepository.findByStatusIn(anyList())).thenReturn(Arrays.asList(travelEntity));
        when(approvalRepository.existsByTravelEntityAndRole(travelEntity, approver.getRoles())).thenReturn(false);
        when(travelEntity.getStatus()).thenReturn(TravelStatus.SUBMITTED);
        when(travelEntity.getUserEntity()).thenReturn(approver);
        when(travelEntity.getTotalAmount()).thenReturn(BigDecimal.ZERO);

        // WHEN
        List<TravelResponseDto> pendingApprovals = approvalService.getPendingApprovals(approver.getEmail());

        // THEN
        assertThat(pendingApprovals).isNotEmpty();
        verify(userRepository, times(1)).findByEmail(approver.getEmail());
        verify(travelRepository, times(1)).findByStatusIn(anyList());
        verify(approvalRepository, times(1)).existsByTravelEntityAndRole(travelEntity, approver.getRoles());
    }

    @Test
    void should_approve_travel_successfully() {
        // GIVEN
        UUID travelId = travelEntity.getTechId();
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelRepository.findByTechId(travelId)).thenReturn(Optional.of(travelEntity));
        when(approvalRepository.existsByTravelEntityAndRole(travelEntity, approver.getRoles())).thenReturn(false);

        // WHEN
        approvalService.approveTravel(travelId, approver.getEmail());

        // THEN
        verify(userRepository, times(1)).findByEmail(approver.getEmail());
        verify(travelRepository, times(1)).findByTechId(travelId);
        verify(approvalRepository, times(1)).save(any(ApprovalEntity.class));
        verify(actionLogService, times(1)).logAction(anyString(), anyLong(), anyLong());
        verify(eventPublisher, times(1)).publishEvent(any(ApprovalEvent.class));
    }

    @Test
    void should_throw_exception_when_approval_already_processed() {
        // GIVEN
        UUID travelId = travelEntity.getTechId();
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelRepository.findByTechId(travelId)).thenReturn(Optional.of(travelEntity));
        when(approvalRepository.existsByTravelEntityAndRole(travelEntity, approver.getRoles())).thenReturn(true);

        // WHEN
        Executable executable = () -> approvalService.approveTravel(travelId, approver.getEmail());

        // THEN
        ApprovalException approvalException = assertThrows(ApprovalException.class, executable);
        assertThat(approvalException.getMessage()).isEqualTo("Approval has already been processed by a " + approver.getRoles());
        verify(approvalRepository, times(1)).existsByTravelEntityAndRole(travelEntity, approver.getRoles());
        verify(approvalRepository, never()).save(any(ApprovalEntity.class));
    }

    @Test
    void should_reject_travel_successfully() {
        // GIVEN
        UUID travelId = travelEntity.getTechId();
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelRepository.findByTechId(travelId)).thenReturn(Optional.of(travelEntity));
        when(approvalRepository.existsByTravelEntityAndRole(travelEntity, approver.getRoles())).thenReturn(false);

        // WHEN
        approvalService.rejectTravel(travelId, approver.getEmail());

        // THEN
        verify(userRepository, times(1)).findByEmail(approver.getEmail());
        verify(travelRepository, times(1)).findByTechId(travelId);
        verify(approvalRepository, times(1)).save(any(ApprovalEntity.class));
        verify(actionLogService, times(1)).logAction(anyString(), anyLong(), anyLong());
        verify(eventPublisher, times(1)).publishEvent(any(ApprovalEvent.class));
    }

    @Test
    void should_throw_exception_when_travel_not_found() {
        // GIVEN
        UUID travelId = UUID.randomUUID();
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelRepository.findByTechId(travelId)).thenReturn(Optional.empty());

        // WHEN
        Executable executable = () -> approvalService.approveTravel(travelId, approver.getEmail());

        // THEN
        TravelException travelException = assertThrows(TravelException.class, executable);
        assertThat(travelException.getMessage()).isEqualTo("Travel not found");
        verify(travelRepository, times(1)).findByTechId(travelId);
        verify(approvalRepository, never()).save(any(ApprovalEntity.class));
    }
}