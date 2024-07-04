package pl.sginko.travelexpense.model.diet.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DietDto {
    @NotNull(message = "Daily allowance cannot be null")
    @Min(value = 0, message = "Number of breakfasts cannot be negative")
    private BigDecimal dailyAllowance;

    @NotNull(message = "Number of breakfasts cannot be null")
    @Min(value = 0, message = "Number of breakfasts cannot be negative")
    private Integer numberOfBreakfasts;

    @NotNull(message = "Number of lunches cannot be null")
    @Min(value = 0, message = "Number of lunches cannot be negative")
    private Integer numberOfLunches ;

    @NotNull(message = "Number of dinners cannot be null")
    @Min(value = 0, message = "Number of dinners cannot be negative")
    private Integer numberOfDinners;

    public DietDto(Integer numberOfBreakfasts, Integer numberOfLunches, Integer numberOfDinners, BigDecimal dailyAllowance) {
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
        this.dailyAllowance = dailyAllowance;
    }
}
