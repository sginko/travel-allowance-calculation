package pl.sginko.travelexpense.logic.diet.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "diet")
public class DietEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @Column(nullable = false)
    private BigDecimal dailyAllowance;

    @Column(nullable = false)
    private Integer numberOfBreakfasts;

    @Column(nullable = false)
    private Integer numberOfLunches;

    @Column(nullable = false)
    private Integer numberOfDinners;

    @Column(nullable = false)
    private BigDecimal dietAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal foodAmount = BigDecimal.ZERO;

    public DietEntity(TravelEntity travelEntity, BigDecimal dailyAllowance, Integer numberOfBreakfasts,
                      Integer numberOfLunches, Integer numberOfDinners) {
        this.dailyAllowance = dailyAllowance;
        this.travelEntity = travelEntity;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
    }
}
