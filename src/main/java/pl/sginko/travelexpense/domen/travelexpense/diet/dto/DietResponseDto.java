package pl.sginko.travelexpense.domen.travelexpense.diet.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DietResponseDto {
    private Long id;
    private Integer numberOfBreakfasts;
    private Integer numberOfLunches;
    private Integer numberOfDinners;
    private BigDecimal foodAmount;
    private BigDecimal dietAmount;
}
