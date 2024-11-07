package pl.sginko.travelexpense.logic.travelexpense.travel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.auth.entity.Roles;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "travel")
public class TravelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID techId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @NotBlank
    @Size(min = 2, max = 50, message = "City name should be between 2 and 50 characters")
    @Column(nullable = false)
    private String fromCity;

    @NotBlank
    @Size(min = 2, max = 50, message = "City name should be between 2 and 50 characters")
    @Column(nullable = false)
    private String toCity;

    @NotNull(message = "The Date field cannot be empty")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "The Time field cannot be empty")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "The Date field cannot be empty")
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull(message = "The Time field cannot be empty")
    @Column(nullable = false)
    private LocalTime endTime;

    @NotNull(message = "Advance payment cannot be null")
    @Min(value = 0, message = "Advance payment cannot be negative")
    @Column(nullable = false)
    private BigDecimal advancePayment;

    @OneToOne(mappedBy = "travelEntity", cascade = CascadeType.ALL)
    private DietEntity dietEntity;

    @OneToOne(mappedBy = "travelEntity", cascade = CascadeType.ALL)
    private OvernightStayEntity overnightStayEntity;

    @OneToOne(mappedBy = "travelEntity", cascade = CascadeType.ALL)
    private TransportCostEntity transportCostEntity;

    @Column(nullable = false)
    private BigDecimal otherExpenses;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TravelStatus status;

    @OneToMany(mappedBy = "travelEntity", cascade = CascadeType.ALL)
    private Set<ApprovalEntity> approvals = new HashSet<>();


    public TravelEntity(String fromCity, String toCity, LocalDate startDate, LocalTime startTime, LocalDate endDate,
                        LocalTime endTime, UserEntity userEntity, BigDecimal advancePayment, BigDecimal otherExpenses) {
        this.techId = UUID.randomUUID();
        this.userEntity = userEntity;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.advancePayment = advancePayment;
        this.otherExpenses = otherExpenses;
        this.status = TravelStatus.SUBMITTED;
        validateDates();
    }

    public void updateDietEntity(DietEntity dietEntity) {
        this.dietEntity = dietEntity;
    }

    public void updateOvernightStayEntity(OvernightStayEntity overnightStayEntity) {
        this.overnightStayEntity = overnightStayEntity;
    }

    public void updateTransportCostEntity(TransportCostEntity transportCostEntity) {
        this.transportCostEntity = transportCostEntity;
    }

    public void updateTotalAmount() {
        BigDecimal dietTotal = dietEntity != null ? dietEntity.calculateDiet() : BigDecimal.ZERO;
        BigDecimal overnightStayTotal = overnightStayEntity != null ? overnightStayEntity.getOvernightStayAmount() : BigDecimal.ZERO;
        BigDecimal transportTotal = transportCostEntity != null ? transportCostEntity.getTransportCostAmount() : BigDecimal.ZERO;
        this.totalAmount = dietTotal.add(overnightStayTotal).add(transportTotal).add(otherExpenses).subtract(advancePayment);
    }

    private void validateDates() {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        if (endDateTime.isBefore(startDateTime)) {
            throw new TravelException("End date and time cannot be before start date and time");
        }
    }

    public void changeStatus(TravelStatus status) {
        this.status = status;
    }

    public void addApproval(ApprovalEntity approval) {
        this.approvals.add(approval);
    }

    public boolean isApprovedByRole(Roles role) {
        return approvals.stream()
                .anyMatch(approval -> approval.getRole() == role && approval.getStatus() == ApprovalStatus.APPROVED);
    }

    public boolean isRejectedByAnyRole() {
        return approvals.stream()
                .anyMatch(approval -> approval.getStatus() == ApprovalStatus.REJECTED);
    }

    public void addApprovalsForRoles(List<UserEntity> accountants, List<UserEntity> managers) {
        for (UserEntity accountant : accountants) {
            this.addApproval(new ApprovalEntity(this, accountant, Roles.ROLE_ACCOUNTANT));
        }

        for (UserEntity manager : managers) {
            this.addApproval(new ApprovalEntity(this, manager, Roles.ROLE_MANAGER));
        }
    }

//    // Метод для проверки, завершены ли все утверждения
//    public boolean areAllApprovalsCompleted() {
//        return approvals.stream()
//                .allMatch(approval -> approval.getStatus() != ApprovalStatus.PENDING);
//    }
//
//    // Метод для проверки, все ли утверждения одобрены
//    public boolean areAllApprovalsApproved() {
//        return approvals.stream()
//                .allMatch(approval -> approval.getStatus() == ApprovalStatus.APPROVED);
//    }
}
