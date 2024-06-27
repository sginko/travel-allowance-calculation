package pl.sginko.travelexpense.model.dietExpenses.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DietExpensesRequestDto {
    @NotNull(message = "Daily allowance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Daily allowance must be non-negative")
    private BigDecimal dailyAllowance;

    @NotNull(message = "Number of breakfasts cannot be null")
    @Min(value = 0, message = "Number of breakfasts cannot be negative")
    private Integer numberOfBreakfasts;

    @NotNull(message = "Number of lunches cannot be null")
    @Min(value = 0, message = "Number of lunches cannot be negative")
    private Integer numberOfLunches;

    @NotNull(message = "Number of dinners cannot be null")
    @Min(value = 0, message = "Number of dinners cannot be negative")
    private Integer numberOfDinners;

    public DietExpensesRequestDto(Integer numberOfBreakfasts, Integer numberOfLunches, Integer numberOfDinners) {
        this.dailyAllowance = BigDecimal.valueOf(45);
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
    }
}
