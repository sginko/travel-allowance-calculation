package pl.sginko.travelexpense.logic.travelexpense.travel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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

//    public long getDurationInHours() {
//        return Duration.between(startTime.atDate(startDate), endTime.atDate(endDate)).toHours();
//    }
//
//    public long getDurationInDays() {
//        long hours = getDurationInHours();
//        long days = hours / 24;
//        if (hours % 24 > 0) {
//            days += 1;
//        }
//        return days;
//    }
}
