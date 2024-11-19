package pl.sginko.travelexpense.domain.travelReport.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sginko.travelexpense.domain.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.domain.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportEditDto;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.user.entity.UserRoles;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelReportEntityTest {
    @Mock
    private DietEntity dietEntity;

    @Mock
    private OvernightStayEntity overnightStayEntity;

    @Mock
    private TransportCostEntity transportCostEntity;

    private TravelReportEntity travelReportEntity;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity("user@example.com", "John", "Doe", "password123");

        travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.of(2024, 11, 5), LocalTime.of(10, 0),
                LocalDate.of(2024, 11, 6), LocalTime.of(15, 0),
                userEntity, BigDecimal.valueOf(50), BigDecimal.valueOf(100));

        travelReportEntity.setDietDetails(dietEntity);
        travelReportEntity.setOvernightStayDetails(overnightStayEntity);
        travelReportEntity.setTransportCostDetails(transportCostEntity);
    }

    @Test
    void should_throw_exception_when_end_date_is_before_start_date() {
        // WHEN
        Executable e = () -> new TravelReportEntity("CityA", "CityB",
                LocalDate.of(2024, 11, 5), LocalTime.of(10, 0),
                LocalDate.of(2024, 11, 4), LocalTime.of(9, 0),
                userEntity, BigDecimal.ZERO, BigDecimal.ZERO);

        // THEN
        assertThrows(TravelReportException.class, e);
    }

    @Test
    void should_calculate_total_amount_correctly() {
        // GIVEN
        when(dietEntity.calculateTotalDiet()).thenReturn(BigDecimal.valueOf(200));
        when(overnightStayEntity.getOvernightStayAmount()).thenReturn(BigDecimal.valueOf(150));
        when(transportCostEntity.getTransportCostAmount()).thenReturn(BigDecimal.valueOf(300));

        // WHEN
        travelReportEntity.calculateTotalAmount();

        // THEN
        BigDecimal expectedTotal = BigDecimal.valueOf(200 + 150 + 300 + 100 - 50);
        assertThat(travelReportEntity.getTotalAmount()).isEqualByComparingTo(expectedTotal);
    }

    @Test
    void should_set_status_to_rejected_if_any_approval_is_rejected() {
        // GIVEN
        ApprovalEntity approval = new ApprovalEntity(travelReportEntity, userEntity, UserRoles.ROLE_MANAGER);
        approval.updateApprovalStatus(ApprovalStatus.REJECTED);
        Set<ApprovalEntity> approvals = new HashSet<>();
        approvals.add(approval);
        travelReportEntity.getApprovals().addAll(approvals);

        // WHEN
        travelReportEntity.updateTravelReportStatusFromApprovals();

        // THEN
        assertThat(travelReportEntity.getStatus()).isEqualTo(TravelReportStatus.REJECTED);
    }

    @Test
    void should_set_status_to_approved_if_both_manager_and_accountant_approve() {
        // GIVEN
        ApprovalEntity managerApproval = new ApprovalEntity(travelReportEntity, userEntity, UserRoles.ROLE_MANAGER);
        managerApproval.updateApprovalStatus(ApprovalStatus.APPROVED);

        ApprovalEntity accountantApproval = new ApprovalEntity(travelReportEntity, userEntity, UserRoles.ROLE_ACCOUNTANT);
        accountantApproval.updateApprovalStatus(ApprovalStatus.APPROVED);

        travelReportEntity.getApprovals().add(managerApproval);
        travelReportEntity.getApprovals().add(accountantApproval);

        // WHEN
        travelReportEntity.updateTravelReportStatusFromApprovals();

        // THEN
        assertThat(travelReportEntity.getStatus()).isEqualTo(TravelReportStatus.APPROVED);
    }

    @Test
    void should_set_status_to_in_process_if_only_one_role_is_approved() {
        // GIVEN
        ApprovalEntity managerApproval = new ApprovalEntity(travelReportEntity, userEntity, UserRoles.ROLE_MANAGER);
        managerApproval.updateApprovalStatus(ApprovalStatus.APPROVED);

        travelReportEntity.getApprovals().add(managerApproval);

        // WHEN
        travelReportEntity.updateTravelReportStatusFromApprovals();

        // THEN
        assertThat(travelReportEntity.getStatus()).isEqualTo(TravelReportStatus.IN_PROCESS);
    }

    @Test
    void should_update_travel_details_correctly() {
        // GIVEN
        DietEditDto dietEditDto = new DietEditDto(BigDecimal.valueOf(50), 2, 1, 1);

        OvernightStayEditDto overnightStayEditDto = new OvernightStayEditDto(1,
                1, BigDecimal.valueOf(180), true);

        TransportCostEditDto transportCostEditDto = new TransportCostEditDto(2,
                BigDecimal.valueOf(100), "Train", BigDecimal.valueOf(230),
                0L, 100L, 0L, 0L);

        TravelReportEditDto travelReportEditDto = new TravelReportEditDto("NewCityA", "NewCityB",
                LocalDate.of(2024, 12, 1), LocalTime.of(8, 30),
                LocalDate.of(2024, 12, 2), LocalTime.of(17, 45), BigDecimal.valueOf(80),
                BigDecimal.valueOf(120), dietEditDto, overnightStayEditDto, transportCostEditDto);

        when(dietEntity.calculateTotalDiet()).thenReturn(BigDecimal.valueOf(200));
        when(overnightStayEntity.getOvernightStayAmount()).thenReturn(BigDecimal.valueOf(180));
        when(transportCostEntity.getTransportCostAmount()).thenReturn(BigDecimal.valueOf(330));

        // WHEN
        travelReportEntity.updateTravelDetails(travelReportEditDto);

        // THEN
        assertThat(travelReportEntity.getFromCity()).isEqualTo("NewCityA");
        assertThat(travelReportEntity.getToCity()).isEqualTo("NewCityB");
        assertThat(travelReportEntity.getStartDate()).isEqualTo(LocalDate.of(2024, 12, 1));
        assertThat(travelReportEntity.getStartTime()).isEqualTo(LocalTime.of(8, 30));
        assertThat(travelReportEntity.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 2));
        assertThat(travelReportEntity.getEndTime()).isEqualTo(LocalTime.of(17, 45));
        assertThat(travelReportEntity.getAdvancePayment()).isEqualByComparingTo(BigDecimal.valueOf(80));
        assertThat(travelReportEntity.getOtherExpenses()).isEqualByComparingTo(BigDecimal.valueOf(120));

        verify(dietEntity).updateDietDetails(dietEditDto);
        verify(overnightStayEntity).updateOvernightStayDetails(overnightStayEditDto);
        verify(transportCostEntity).updateTransportCostDetails(transportCostEditDto);

        BigDecimal expectedTotal = BigDecimal.valueOf(200 + 180 + 330 + 120 - 80);
        assertThat(travelReportEntity.getTotalAmount()).isEqualByComparingTo(expectedTotal);
    }
}
