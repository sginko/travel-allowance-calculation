package pl.sginko.travelexpense.logic.transport;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @Column(nullable = false)
    private Integer inputtedDaysNumberForTransportCost; //Ilość dób, za które przysługuje ryczałt za dojazdy

    @Column(nullable = false)
    private BigDecimal undocumentedLocalTransportCost; //wyliczona w zaleznosci od ilosci dni

    @Column(nullable = false)
    private BigDecimal documentedLocalTransportCost; //Kwota z tytułu zwrotu udokumentowanych kosztów dojazdów (wymagana zgoda pracodawcy)


    //Rodzaj transportu przejazdu. Wybieram w menu.
    private String nameOfTransport;

    //Przejazd transportem publicznym w zlotych
    @Column(nullable = false)
    private BigDecimal transportCost;


    //Przejazd niebędącym własnością pracodawcy (liczba faktycznie przejechanych kilometrów)
    @Column(nullable = false)
    private BigDecimal costByCarEngineUpTo900cc = BigDecimal.valueOf(0.89);

    @Column(nullable = false)
    private BigDecimal costByCarEngineAbove900cc = BigDecimal.valueOf(1.15);

    @Column(nullable = false)
    private BigDecimal costByMotorcycle = BigDecimal.valueOf(0.69);

    @Column(nullable = false)
    private BigDecimal costByMoped = BigDecimal.valueOf(0.42);

    @NotNull(message = "Kilometers by car engine up to 900cc cannot be null")
    @Min(value = 0, message = "Kilometers by car engine up to 900cc cannot be negative")
    private Long kilometersByCarEngineUpTo900cc;

    @NotNull(message = "Kilometers by car engine above 900cc cannot be null")
    @Min(value = 0, message = "Kilometers by car engine above 900cc cannot be negative")
    private Long kilometersByCarEngineAbove900cc;

    @NotNull(message = "Kilometers by motorcycle cannot be null")
    @Min(value = 0, message = "Kilometers by motorcycle cannot be negative")
    private Long kilometersByMotorcycle;

    @NotNull(message = "Kilometers by moped cannot be null")
    @Min(value = 0, message = "Kilometers by moped cannot be negative")
    private Long kilometersByMoped;




    public TransportCostEntity(TravelEntity travelEntity, BigDecimal transportCost, Integer inputtedDaysNumberForTransportCost) {
        this.travelEntity = travelEntity;
        this.transportCost = transportCost;
        this.inputtedDaysNumberForTransportCost = inputtedDaysNumberForTransportCost;
    }

    public void updateLocalTransportCost(BigDecimal documentedTransportCostAmount) {
        this.documentedLocalTransportCost = documentedTransportCostAmount;
    }
}
