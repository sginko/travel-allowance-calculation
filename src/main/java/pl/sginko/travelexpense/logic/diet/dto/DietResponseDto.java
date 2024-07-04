package pl.sginko.travelexpense.logic.diet.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class DietResponseDto {
    private Long id;
    private BigDecimal dietAmount;
    private Integer numberOfBreakfasts;
    private Integer numberOfLunches;
    private Integer numberOfDinners;
}
