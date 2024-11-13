package pl.sginko.travelexpense.logic.travelexpense.travelReport.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.logic.user.entity.Roles;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
        MockitoAnnotations.openMocks(this);
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
        ApprovalEntity approval = new ApprovalEntity(travelReportEntity, userEntity, Roles.ROLE_MANAGER);
        approval.updateStatus(ApprovalStatus.REJECTED);
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
        ApprovalEntity managerApproval = new ApprovalEntity(travelReportEntity, userEntity, Roles.ROLE_MANAGER);
        managerApproval.updateStatus(ApprovalStatus.APPROVED);

        ApprovalEntity accountantApproval = new ApprovalEntity(travelReportEntity, userEntity, Roles.ROLE_ACCOUNTANT);
        accountantApproval.updateStatus(ApprovalStatus.APPROVED);

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
        ApprovalEntity managerApproval = new ApprovalEntity(travelReportEntity, userEntity, Roles.ROLE_MANAGER);
        managerApproval.updateStatus(ApprovalStatus.APPROVED);

        travelReportEntity.getApprovals().add(managerApproval);

        // WHEN
        travelReportEntity.updateTravelReportStatusFromApprovals();

        // THEN
        assertThat(travelReportEntity.getStatus()).isEqualTo(TravelReportStatus.IN_PROCESS);
    }
}
