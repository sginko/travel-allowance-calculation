package pl.sginko.travelallowance.model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)

@Entity
public class TravelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private BigDecimal dailyAllowance;

    private Integer breakfastQuantity;
    private Integer lunchQuantity;
    private Integer dinnerQuantity;

    private BigDecimal costOfTotalExpense;
}