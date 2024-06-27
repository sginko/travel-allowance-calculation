package pl.sginko.travelexpense.model.dietExpenses.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "diet")
public class DietExpensesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Number of breakfasts cannot be null")
    @Min(value = 0, message = "Number of breakfasts cannot be negative")
    @Column(nullable = false)
    private Integer numberOfBreakfasts;

    @NotNull(message = "Number of lunches cannot be null")
    @Min(value = 0, message = "Number of lunches cannot be negative")
    @Column(nullable = false)
    private Integer numberOfLunches;

    @NotNull(message = "Number of dinners cannot be null")
    @Min(value = 0, message = "Number of dinners cannot be negative")
    @Column(nullable = false)
    private Integer numberOfDinners;

    @NotNull(message = "Daily allowance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Daily allowance must be non-negative")
    @Column(nullable = false)
    private BigDecimal dailyAllowance;

    @NotNull(message = "Diet amount cannot be null")
    @Column(nullable = false)
    private BigDecimal dietAmount = BigDecimal.ZERO;

    @NotNull(message = "Food amount cannot be null")
    @Column(nullable = false)
    private BigDecimal foodAmount = BigDecimal.ZERO;

    public DietExpensesEntity(Integer numberOfBreakfasts, Integer numberOfLunches, Integer numberOfDinners) {
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
    }
}
