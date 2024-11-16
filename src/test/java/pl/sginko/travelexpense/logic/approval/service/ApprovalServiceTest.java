package pl.sginko.travelexpense.logic.approval.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.sginko.travelexpense.logic.actionLog.service.ActionLogService;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.logic.approval.event.ApprovalEvent;
import pl.sginko.travelexpense.logic.approval.exception.ApprovalException;
import pl.sginko.travelexpense.logic.approval.repository.ApprovalRepository;
import pl.sginko.travelexpense.logic.travelexpense.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.mapper.TravelReportMapper;
import pl.sginko.travelexpense.logic.user.entity.Roles;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.repository.UserRepository;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.repository.TravelReportRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private TravelReportRepository travelReportRepository;

    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActionLogService actionLogService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private DietMapper dietMapper;

    @Mock
    private OvernightStayMapper overnightStayMapper;

    @Mock
    private TransportCostMapper transportCostMapper;

    @Mock
    private TravelReportMapper travelReportMapper;

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    private UserEntity approver;
    private TravelReportEntity travelReportEntity;
    private UUID travelId;

    @BeforeEach
    void setUp() {
        approver = new UserEntity("approver@example.com", "Approver", "User", "password");

        approver.changeRoleToManager();

//        when(approver.getId()).thenReturn(1L);

        travelReportEntity = new TravelReportEntity("CityA", "CityB", LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0), approver, BigDecimal.ZERO, BigDecimal.ZERO);

//        when(travelReportEntity.getId()).thenReturn(2L);

        travelReportEntity.updateStatus(TravelReportStatus.SUBMITTED);

        travelId = travelReportEntity.getTechId();
    }

    @Test
    void should_throw_exception_when_rejecting_already_processed_travel() {
        // GIVEN
        when(userRepository.findByEmail(approver.getEmail())).thenReturn(Optional.of(approver));
        when(travelReportRepository.findByTechId(travelId)).thenReturn(Optional.of(travelReportEntity));
        when(approvalRepository.existsByTravelReportEntityAndRole(eq(travelReportEntity), eq(approver.getRoles()))).thenReturn(true);

        // WHEN
        Executable e = () -> approvalService.rejectTravel(travelId, approver.getEmail());

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
        Executable e = () -> approvalService.rejectTravel(travelId, approver.getEmail());

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
        List<TravelReportResponseDto> pendingApprovals = approvalService.getPendingApprovals(approver.getEmail());

        // THEN
        assertThat(pendingApprovals).hasSize(2);
        assertThat(pendingApprovals)
                .extracting(travelReportResponseDto -> travelReportResponseDto.getFromCity())
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
        List<TravelReportResponseDto> pendingApprovals = approvalService.getPendingApprovals(approver.getEmail());

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
        List<TravelReportResponseDto> pendingApprovals = approvalService.getPendingApprovals(approver.getEmail());

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
