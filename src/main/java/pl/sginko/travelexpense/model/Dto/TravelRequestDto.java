package pl.sginko.travelexpense.model.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelRequestDto {
//    private LocalDateTime startDateTime;
//    private LocalDateTime endDateTime;

    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);
    private Integer numberOfBreakfasts;
    private Integer numberOfLunches;
    private Integer numberOfDinners;

    public TravelRequestDto(LocalDate startDateTravel, LocalTime startTimeTravel, LocalDate finishDateTravel, LocalTime finishTimeTravel,
                            Integer numberOfBreakfasts, Integer numberOfLunches, Integer numberOfDinners) {
        this.startDate = startDateTravel;
        this.startTime = startTimeTravel;
        this.endDate = finishDateTravel;
        this.endTime = finishTimeTravel;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
    }
}