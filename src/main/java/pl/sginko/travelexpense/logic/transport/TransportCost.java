package pl.sginko.travelexpense.logic.transport;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "transport")
public class TransportCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @Column(nullable = false)
    private BigDecimal transportCost;

    @Column(nullable = false)
    private BigDecimal localTransportCost;




}
