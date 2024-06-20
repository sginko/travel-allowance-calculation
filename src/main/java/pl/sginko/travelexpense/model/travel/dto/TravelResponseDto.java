package pl.sginko.travelexpense.model.travel.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelResponseDto {
    private Long id;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Integer numberOfBreakfasts;
    private Integer numberOfLunches;
    private Integer numberOfDinners;
    private BigDecimal totalAmount;
    private BigDecimal dietAmount;
    private BigDecimal foodAmount;
    private Long pesel;

    public TravelResponseDto(Long id, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime,
                             Integer numberOfBreakfasts, Integer numberOfLunches, Integer numberOfDinners, BigDecimal totalAmount,
                             BigDecimal dietAmount, BigDecimal foodAmount, Long pesel) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
        this.totalAmount = totalAmount;
        this.dietAmount = dietAmount;
        this.foodAmount = foodAmount;
        this.pesel=pesel;
    }
}