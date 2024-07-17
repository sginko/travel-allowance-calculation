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


    //Dojazdy środkami komunikacji miejscowej (w miejscu delegacji)
    @NotNull(message = "inputtedDaysNumberForTransportCost cannot be null")
    @Column(nullable = false)
    private Integer inputtedDaysNumberForTransportCost; //Ilość dób, za które przysługuje ryczałt za dojazdy

    @Column(nullable = false)
    private BigDecimal undocumentedLocalTransportCost; //wyliczona w zaleznosci od ilosci dni

    @Column(nullable = false)
    private BigDecimal documentedLocalTransportCost; //Kwota z tytułu zwrotu udokumentowanych kosztów dojazdów (wymagana zgoda pracodawcy)


    //Rodzaj transportu przejazdu. Wybieram w menu.
    @NotBlank(message = "meansOfTransport cannot be blank")
    @Column(nullable = false)
    private String meansOfTransport;

    //Przejazd transportem publicznym w zlotych
    @Column(nullable = false)
    private BigDecimal costOfTravelByPublicTransport;


    //Przejazd niebędącym własnością pracodawcy (liczba faktycznie przejechanych kilometrów)
    @Column(nullable = false)
    private final BigDecimal COST_BY_CAR_ENGINE_UP_TO_900_CC = BigDecimal.valueOf(0.89);

    @Column(nullable = false)
    private final BigDecimal COST_BY_CAR_ENGINE_ABOVE_TO_900_CC = BigDecimal.valueOf(1.15);

    @Column(nullable = false)
    private final BigDecimal COST_BY_MOTORCYCLE = BigDecimal.valueOf(0.69);

    @Column(nullable = false)
    private final BigDecimal COST_BY_MOPED = BigDecimal.valueOf(0.42);

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

    public TransportCostEntity(TravelEntity travelEntity, Integer inputtedDaysNumberForTransportCost, BigDecimal undocumentedLocalTransportCost,
                               BigDecimal documentedLocalTransportCost, String meansOfTransport, BigDecimal costOfTravelByPublicTransport,
                               Long kilometersByCarEngineUpTo900cc, Long kilometersByCarEngineAbove900cc, Long kilometersByMotorcycle,
                               Long kilometersByMoped, BigDecimal costOfTravelByOwnTransport, BigDecimal transportCostAmount) {
        this.travelEntity = travelEntity;
        this.inputtedDaysNumberForTransportCost = inputtedDaysNumberForTransportCost;
        this.undocumentedLocalTransportCost = undocumentedLocalTransportCost;
        this.documentedLocalTransportCost = documentedLocalTransportCost;
        this.meansOfTransport = meansOfTransport;
        this.costOfTravelByPublicTransport = costOfTravelByPublicTransport;
        this.kilometersByCarEngineUpTo900cc = kilometersByCarEngineUpTo900cc;
        this.kilometersByCarEngineAbove900cc = kilometersByCarEngineAbove900cc;
        this.kilometersByMotorcycle = kilometersByMotorcycle;
        this.kilometersByMoped = kilometersByMoped;
        this.costOfTravelByOwnTransport = costOfTravelByOwnTransport;
        this.transportCostAmount = transportCostAmount;
    }

    public void updateUndocumentedLocalTransportCost(BigDecimal undocumentedLocalTransportCost) {
        this.undocumentedLocalTransportCost = undocumentedLocalTransportCost;
    }
}
