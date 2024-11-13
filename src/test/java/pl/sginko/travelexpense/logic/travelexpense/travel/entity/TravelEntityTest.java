package pl.sginko.travelexpense.logic.travelexpense.travel.entity;

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
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;
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

class TravelEntityTest {
    @Mock
    private DietEntity dietEntity;

    @Mock
    private OvernightStayEntity overnightStayEntity;

    @Mock
    private TransportCostEntity transportCostEntity;

    private TravelEntity travelEntity;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity("user@example.com", "John", "Doe", "password123");

        travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.of(2024, 11, 5), LocalTime.of(10, 0),
                LocalDate.of(2024, 11, 6), LocalTime.of(15, 0),
                userEntity, BigDecimal.valueOf(50), BigDecimal.valueOf(100));

        travelEntity.setDietDetails(dietEntity);
        travelEntity.setOvernightStayDetails(overnightStayEntity);
        travelEntity.setTransportCostDetails(transportCostEntity);
    }

    @Test
    void should_throw_exception_when_end_date_is_before_start_date() {
        // WHEN
        Executable e = () -> new TravelEntity("CityA", "CityB",
                LocalDate.of(2024, 11, 5), LocalTime.of(10, 0),
                LocalDate.of(2024, 11, 4), LocalTime.of(9, 0),
                userEntity, BigDecimal.ZERO, BigDecimal.ZERO);

        // THEN
        assertThrows(TravelException.class, e);
    }

    @Test
    void should_calculate_total_amount_correctly() {
        // GIVEN
        when(dietEntity.calculateTotalDiet()).thenReturn(BigDecimal.valueOf(200));
        when(overnightStayEntity.getOvernightStayAmount()).thenReturn(BigDecimal.valueOf(150));
        when(transportCostEntity.getTransportCostAmount()).thenReturn(BigDecimal.valueOf(300));

        // WHEN
        travelEntity.calculateTotalAmount();

        // THEN
        BigDecimal expectedTotal = BigDecimal.valueOf(200 + 150 + 300 + 100 - 50);
        assertThat(travelEntity.getTotalAmount()).isEqualByComparingTo(expectedTotal);
    }

    @Test
    void should_set_status_to_rejected_if_any_approval_is_rejected() {
        // GIVEN
        ApprovalEntity approval = new ApprovalEntity(travelEntity, userEntity, Roles.ROLE_MANAGER);
        approval.updateStatus(ApprovalStatus.REJECTED);
        Set<ApprovalEntity> approvals = new HashSet<>();
        approvals.add(approval);
        travelEntity.getApprovals().addAll(approvals);

        // WHEN
        travelEntity.updateTravelReportStatusFromApprovals();

        // THEN
        assertThat(travelEntity.getStatus()).isEqualTo(TravelStatus.REJECTED);
    }

    @Test
    void should_set_status_to_approved_if_both_manager_and_accountant_approve() {
        // GIVEN
        ApprovalEntity managerApproval = new ApprovalEntity(travelEntity, userEntity, Roles.ROLE_MANAGER);
        managerApproval.updateStatus(ApprovalStatus.APPROVED);

        ApprovalEntity accountantApproval = new ApprovalEntity(travelEntity, userEntity, Roles.ROLE_ACCOUNTANT);
        accountantApproval.updateStatus(ApprovalStatus.APPROVED);

        travelEntity.getApprovals().add(managerApproval);
        travelEntity.getApprovals().add(accountantApproval);

        // WHEN
        travelEntity.updateTravelReportStatusFromApprovals();

        // THEN
        assertThat(travelEntity.getStatus()).isEqualTo(TravelStatus.APPROVED);
    }

    @Test
    void should_set_status_to_in_process_if_only_one_role_is_approved() {
        // GIVEN
        ApprovalEntity managerApproval = new ApprovalEntity(travelEntity, userEntity, Roles.ROLE_MANAGER);
        managerApproval.updateStatus(ApprovalStatus.APPROVED);

        travelEntity.getApprovals().add(managerApproval);

        // WHEN
        travelEntity.updateTravelReportStatusFromApprovals();

        // THEN
        assertThat(travelEntity.getStatus()).isEqualTo(TravelStatus.IN_PROCESS);
    }
}
