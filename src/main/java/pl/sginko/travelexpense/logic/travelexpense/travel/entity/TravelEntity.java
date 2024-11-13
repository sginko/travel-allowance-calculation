package pl.sginko.travelexpense.logic.travelexpense.travel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelEditDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.user.entity.Roles;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
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

    @OneToOne(mappedBy = "travelEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private DietEntity dietEntity;

    @OneToOne(mappedBy = "travelEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private OvernightStayEntity overnightStayEntity;

    @OneToOne(mappedBy = "travelEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private TransportCostEntity transportCostEntity;

    @Column(nullable = false)
    private BigDecimal otherExpenses;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TravelStatus status;

    @OneToMany(mappedBy = "travelEntity", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void setDietDetails(DietEntity dietEntity) {
        this.dietEntity = dietEntity;
    }

    public void setOvernightStayDetails(OvernightStayEntity overnightStayEntity) {
        this.overnightStayEntity = overnightStayEntity;
    }

    public void setTransportCostDetails(TransportCostEntity transportCostEntity) {
        this.transportCostEntity = transportCostEntity;
    }

    public void calculateTotalAmount() {
        BigDecimal dietTotal = dietEntity != null ? dietEntity.calculateTotalDiet() : BigDecimal.ZERO;
        BigDecimal overnightStayTotal = overnightStayEntity != null ? overnightStayEntity.getOvernightStayAmount() : BigDecimal.ZERO;
        BigDecimal transportTotal = transportCostEntity != null ? transportCostEntity.getTransportCostAmount() : BigDecimal.ZERO;

        this.totalAmount = dietTotal.add(overnightStayTotal).add(transportTotal).add(otherExpenses).subtract(advancePayment);
    }

    public void updateTravelReportStatusFromApprovals() {
        if (anyApprovalRejected()) {
            updateStatus(TravelStatus.REJECTED);
        } else {
            boolean accountantApproved = isApprovedByAtLeastOneRole(Roles.ROLE_ACCOUNTANT);
            boolean managerApproved = isApprovedByAtLeastOneRole(Roles.ROLE_MANAGER);

            if (accountantApproved && managerApproved) {
                updateStatus(TravelStatus.APPROVED);
            } else {
                updateStatus(TravelStatus.IN_PROCESS);
            }
        }
    }

    public void updateStatus(TravelStatus status) {
        this.status = status;
    }

    public void updateTravelDetails(TravelEditDto travelEditDto) {
        this.fromCity = travelEditDto.getFromCity();
        this.toCity = travelEditDto.getToCity();
        this.startDate = travelEditDto.getStartDate();
        this.startTime = travelEditDto.getStartTime();
        this.endDate = travelEditDto.getEndDate();
        this.endTime = travelEditDto.getEndTime();
        this.advancePayment = travelEditDto.getAdvancePayment();
        this.otherExpenses = travelEditDto.getOtherExpenses();

        validateDates();

        if (this.dietEntity != null && travelEditDto.getDietEditDtoDto() != null) {
            this.dietEntity.updateDietDetails(travelEditDto.getDietEditDtoDto());
        }

        if (this.overnightStayEntity != null && travelEditDto.getOvernightStayEditDto() != null) {
            this.overnightStayEntity.updateOvernightStayDetails(travelEditDto.getOvernightStayEditDto());
        }

        if (this.transportCostEntity != null && travelEditDto.getTransportCostEditDto() != null) {
            this.transportCostEntity.updateTransportCostDetails(travelEditDto.getTransportCostEditDto());
        }

        calculateTotalAmount();
    }

    private boolean anyApprovalRejected() {
        return approvals.stream()
                .anyMatch(approval -> approval.getStatus() == ApprovalStatus.REJECTED);
    }

    private boolean isApprovedByAtLeastOneRole(Roles role) {
        return approvals.stream()
                .filter(approval -> approval.getRole() == role)
                .anyMatch(approval -> approval.getStatus() == ApprovalStatus.APPROVED);
    }

    private void validateDates() {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        if (endDateTime.isBefore(startDateTime)) {
            throw new TravelException("End date and time cannot be before start date and time");
        }
    }
}
