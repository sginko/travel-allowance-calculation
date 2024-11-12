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
    private UUID travelId;

    @BeforeEach
    void setUp() {
        approver = new UserEntity("approver@example.com", "Approver", "User", "password");
        approver.changeRoleToManager();

        travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                approver, BigDecimal.ZERO, BigDecimal.ZERO);
        travelId = travelEntity.getTechId();
    }

//    @Test
//    void should_approve_travel_successfully() {
//        // GIVEN
//        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
//        when(travelRepository.findByTechId(travelId)).thenReturn(Optional.of(travelEntity));
//        when(approvalRepository.existsByTravelEntityAndRole(eq(travelEntity), eq(approver.getRoles()))).thenReturn(false);
//
//        // WHEN
//        approvalService.approveTravel(travelId, approver.getEmail());
//
//        // THEN
//        verify(userRepository).findByEmail(eq(approver.getEmail()));
//        verify(travelRepository).findByTechId(eq(travelId));
//        verify(approvalRepository).save(any(ApprovalEntity.class));
//        verify(actionLogService).logAction(any(String.class), any(Long.class), eq(approver.getId()));
//        verify(eventPublisher).publishEvent(any(ApprovalEvent.class));
//    }

    @Test
    void should_throw_exception_when_approval_already_processed() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelRepository.findByTechId(travelId)).thenReturn(Optional.of(travelEntity));
        when(approvalRepository.existsByTravelEntityAndRole(eq(travelEntity), eq(approver.getRoles()))).thenReturn(true);

        // WHEN
        Executable executable = () -> approvalService.approveTravel(travelId, approver.getEmail());

        // THEN
        ApprovalException exception = assertThrows(ApprovalException.class, executable);
        assertThat(exception.getMessage()).isEqualTo("Approval has already been processed by a " + approver.getRoles());
        verify(approvalRepository, never()).save(any(ApprovalEntity.class));
    }

    @Test
    void should_throw_exception_when_travel_not_found() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelRepository.findByTechId(travelId)).thenReturn(Optional.empty());

        // WHEN
        Executable executable = () -> approvalService.approveTravel(travelId, approver.getEmail());

        // THEN
        TravelException exception = assertThrows(TravelException.class, executable);
        assertThat(exception.getMessage()).isEqualTo("Travel not found");
        verify(approvalRepository, never()).save(any(ApprovalEntity.class));
    }
}