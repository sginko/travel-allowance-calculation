package pl.sginko.travelexpense.logic.transport.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "transport")
public class TransportCostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @NotNull(message = "inputtedDaysNumberForTransportCost cannot be null")
    @Column(nullable = false)
    private Integer inputtedDaysNumberForUndocumentedTransportCost;

    @Column(nullable = false)
    private BigDecimal undocumentedLocalTransportCost;

    @Column(nullable = false)
    private BigDecimal documentedLocalTransportCost;

    @NotBlank(message = "meansOfTransport cannot be blank")
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
    private BigDecimal transportCostAmount;

    public TransportCostEntity(TravelEntity travelEntity, Integer inputtedDaysNumberForUndocumentedTransportCost, BigDecimal documentedLocalTransportCost,
                               String meansOfTransport, BigDecimal costOfTravelByPublicTransport, Long kilometersByCarEngineUpTo900cc,
                               Long kilometersByCarEngineAbove900cc, Long kilometersByMotorcycle, Long kilometersByMoped) {
        this.travelEntity = travelEntity;
        this.inputtedDaysNumberForUndocumentedTransportCost = inputtedDaysNumberForUndocumentedTransportCost;
        this.documentedLocalTransportCost = documentedLocalTransportCost;
        this.meansOfTransport = meansOfTransport;
        this.costOfTravelByPublicTransport = costOfTravelByPublicTransport;
        this.kilometersByCarEngineUpTo900cc = kilometersByCarEngineUpTo900cc;
        this.kilometersByCarEngineAbove900cc = kilometersByCarEngineAbove900cc;
        this.kilometersByMotorcycle = kilometersByMotorcycle;
        this.kilometersByMoped = kilometersByMoped;
    }

    public void updateUndocumentedLocalTransportCost(BigDecimal undocumentedLocalTransportCost) {
        this.undocumentedLocalTransportCost = undocumentedLocalTransportCost;
    }

    public void updateCostOfTravelByPublicTransport(BigDecimal costOfTravelByPublicTransport) {
        this.costOfTravelByPublicTransport = costOfTravelByPublicTransport;
    }

    public void updateCostOfTravelByOwnTransport(BigDecimal costOfTravelByOwnTransport) {
        this.costOfTravelByOwnTransport = costOfTravelByOwnTransport;
    }

    public void updateTransportCostAmount(BigDecimal transportCostAmount) {
        this.transportCostAmount = transportCostAmount;
    }
}
