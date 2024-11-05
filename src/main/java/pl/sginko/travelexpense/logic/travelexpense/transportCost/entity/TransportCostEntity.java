package pl.sginko.travelexpense.logic.travelexpense.transportCost.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.exception.TransportException;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.Duration;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "transport")
public class TransportCostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @NotNull(message = "Days For Undocumented Transport Cost cannot be null")
    @Column(nullable = false)
    private Integer daysForUndocumentedTransportCost;

    @Column(nullable = false)
    private BigDecimal undocumentedLocalTransportCost;

    @Column(nullable = false)
    private BigDecimal documentedLocalTransportCost;

    @NotBlank(message = "Means Of Transport cannot be blank")
    @Column(nullable = false)
    private String meansOfTransport;

    @Column(nullable = false)
    private BigDecimal costOfTravelByPublicTransport;

    @NotNull(message = "Kilometers by car engine up to 900cc cannot be null")
    @Min(value = 0, message = "Kilometers by car engine up to 900cc cannot be negative")
    @Column(nullable = false)
    private Long kilometersByCarEngineUpTo900cc;

    @NotNull(message = "Kilometers by car engine above 900cc cannot be null")
    @Min(value = 0, message = "Kilometers by car engine above 900cc cannot be negative")
    @Column(nullable = false)
    private Long kilometersByCarEngineAbove900cc;

    @NotNull(message = "Kilometers by motorcycle cannot be null")
    @Min(value = 0, message = "Kilometers by motorcycle cannot be negative")
    @Column(nullable = false)
    private Long kilometersByMotorcycle;

    @NotNull(message = "Kilometers by moped cannot be null")
    @Min(value = 0, message = "Kilometers by moped cannot be negative")
    @Column(nullable = false)
    private Long kilometersByMoped;

    @Column(nullable = false)
    private BigDecimal costOfTravelByOwnTransport;

    @Column(nullable = false)
    private BigDecimal totalCostOfTravelByOwnAndPublicTransport;

    @Column(nullable = false)
    private BigDecimal transportCostAmount;

    private static final BigDecimal COST_BY_CAR_ENGINE_UP_TO_900_CC = BigDecimal.valueOf(0.89);
    private static final BigDecimal COST_BY_CAR_ENGINE_ABOVE_900_CC = BigDecimal.valueOf(1.15);
    private static final BigDecimal COST_BY_MOTORCYCLE = BigDecimal.valueOf(0.69);
    private static final BigDecimal COST_BY_MOPED = BigDecimal.valueOf(0.42);

    public TransportCostEntity(TravelEntity travelEntity, Integer daysForUndocumentedTransportCost,
                               BigDecimal documentedLocalTransportCost, String meansOfTransport,
                               BigDecimal costOfTravelByPublicTransport, Long kilometersByCarEngineUpTo900cc,
                               Long kilometersByCarEngineAbove900cc, Long kilometersByMotorcycle, Long kilometersByMoped) {
        this.travelEntity = travelEntity;
        this.daysForUndocumentedTransportCost = daysForUndocumentedTransportCost != null ? daysForUndocumentedTransportCost : 0;
        this.documentedLocalTransportCost = documentedLocalTransportCost != null ? documentedLocalTransportCost : BigDecimal.ZERO;
        this.meansOfTransport = meansOfTransport != null ? meansOfTransport : "";
        this.costOfTravelByPublicTransport = costOfTravelByPublicTransport != null ? costOfTravelByPublicTransport : BigDecimal.ZERO;
        this.kilometersByCarEngineUpTo900cc = kilometersByCarEngineUpTo900cc != null ? kilometersByCarEngineUpTo900cc : 0;
        this.kilometersByCarEngineAbove900cc = kilometersByCarEngineAbove900cc != null ? kilometersByCarEngineAbove900cc : 0;
        this.kilometersByMotorcycle = kilometersByMotorcycle != null ? kilometersByMotorcycle : 0;
        this.kilometersByMoped = kilometersByMoped != null ? kilometersByMoped : 0;

        this.costOfTravelByOwnTransport = calculateCostOfTravelByOwnTransport();
        this.undocumentedLocalTransportCost = calculateUndocumentedLocalTransportCost();
        this.transportCostAmount = calculateTransportCost();
        this.totalCostOfTravelByOwnAndPublicTransport = calculateTotalCostOfTravelByOwnAndPublicTransport();
    }

    public BigDecimal calculateTransportCost() {
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (documentedLocalTransportCost.compareTo(BigDecimal.ZERO) > 0) {
            totalAmount = documentedLocalTransportCost;
        } else {
            totalAmount = undocumentedLocalTransportCost;
        }

        return totalAmount.add(costOfTravelByPublicTransport).add(costOfTravelByOwnTransport);

//        return calculateUndocumentedLocalTransportCost()
//                .add(documentedLocalTransportCost)
//                .add(costOfTravelByPublicTransport)
//                .add(calculateCostOfTravelByOwnTransport());
    }

    private BigDecimal calculateTotalCostOfTravelByOwnAndPublicTransport() {
        return costOfTravelByPublicTransport.add(calculateCostOfTravelByOwnTransport());
    }

    private BigDecimal calculateUndocumentedLocalTransportCost() {
        if (documentedLocalTransportCost.compareTo(BigDecimal.ZERO) > 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal dailyAllowance = travelEntity.getDietEntity().getDailyAllowance();
        BigDecimal dailyUndocumentedLocalTransportCost = dailyAllowance.multiply(BigDecimal.valueOf(0.20));
        Long daysInTravel = getDaysInTravel();

        if (daysForUndocumentedTransportCost > daysInTravel) {
            throw new TransportException("The number of days entered for undocumented Local Transport Costs is greater than the number of days on the trip");
        }

        return dailyUndocumentedLocalTransportCost.multiply(BigDecimal.valueOf(daysForUndocumentedTransportCost));
    }

    private BigDecimal calculateCostOfTravelByOwnTransport() {
        BigDecimal amountCostByCarEngineUpTo900Cc = COST_BY_CAR_ENGINE_UP_TO_900_CC.multiply(BigDecimal.valueOf(kilometersByCarEngineUpTo900cc));
        BigDecimal amountCostByCarEngineAboveTo900Cc = COST_BY_CAR_ENGINE_ABOVE_900_CC.multiply(BigDecimal.valueOf(kilometersByCarEngineAbove900cc));
        BigDecimal amountCostByMotorcycle = COST_BY_MOTORCYCLE.multiply(BigDecimal.valueOf(kilometersByMotorcycle));
        BigDecimal amountCostByMoped = COST_BY_MOPED.multiply(BigDecimal.valueOf(kilometersByMoped));

        return amountCostByCarEngineUpTo900Cc.add(amountCostByCarEngineAboveTo900Cc).add(amountCostByMotorcycle).add(amountCostByMoped);
    }

    private Long getDaysInTravel() {
        long hoursInTravel = getDurationInHours();
        long daysInTravel = hoursInTravel / 24;
        long remainingHours = hoursInTravel % 24;

        if (remainingHours > 0) {
            daysInTravel++;
        }
        return daysInTravel;
//        return remainingHours > 0 ? daysInTravel + 1 : daysInTravel;
    }

    private long getDurationInHours() {
        return Duration.between(travelEntity.getStartTime().atDate(travelEntity.getStartDate()),
                travelEntity.getEndTime().atDate(travelEntity.getEndDate())).toHours();
    }
}
