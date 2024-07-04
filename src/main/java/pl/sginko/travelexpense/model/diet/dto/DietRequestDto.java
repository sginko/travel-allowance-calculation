package pl.sginko.travelexpense.model.diet.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DietRequestDto {
    @NotNull(message = "Number of breakfasts cannot be null")
    @Min(value = 0, message = "Number of breakfasts cannot be negative")
    private Integer numberOfBreakfasts = 0;

    @NotNull(message = "Number of lunches cannot be null")
    @Min(value = 0, message = "Number of lunches cannot be negative")
    private Integer numberOfLunches = 0;

    @NotNull(message = "Number of dinners cannot be null")
    @Min(value = 0, message = "Number of dinners cannot be negative")
    private Integer numberOfDinners = 0;

    public DietRequestDto(Integer numberOfBreakfasts, Integer numberOfLunches, Integer numberOfDinners) {
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
    }
}
