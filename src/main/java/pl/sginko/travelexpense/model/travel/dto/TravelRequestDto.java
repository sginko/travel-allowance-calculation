package pl.sginko.travelexpense.model.travel.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelRequestDto {
    private Long pesel;
    private String cityFrom;
    private String cityTo;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);
    private Integer numberOfBreakfasts;
    private Integer numberOfLunches;
    private Integer numberOfDinners;

    public TravelRequestDto(Long pesel, String cityFrom, String cityTo, LocalDate startDate, LocalTime startTime,
                            LocalDate endDate, LocalTime endTime, Integer numberOfBreakfasts, Integer numberOfLunches,
                            Integer numberOfDinners) {
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.pesel = pesel;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
    }
}
