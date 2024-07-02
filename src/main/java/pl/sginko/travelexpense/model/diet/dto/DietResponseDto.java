package pl.sginko.travelexpense.model.diet.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DietResponseDto {
    private Long id;
    private BigDecimal dietAmount;
    private Integer numberOfBreakfasts;
    private Integer numberOfLunches;
    private Integer numberOfDinners;

    public DietResponseDto(Long id, BigDecimal dietAmount, Integer numberOfBreakfasts, Integer numberOfLunches, Integer numberOfDinners) {
        this.id = id;
        this.dietAmount = dietAmount;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
    }
}
