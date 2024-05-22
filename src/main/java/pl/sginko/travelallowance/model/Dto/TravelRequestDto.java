package pl.sginko.travelallowance.model.Dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class TravelRequestDto {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);
    private Integer breakfastQuantity;
    private Integer lunchQuantity;
    private Integer dinnerQuantity;

    public TravelRequestDto(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer breakfastQuantity,
                            Integer lunchQuantity, Integer dinnerQuantity) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.breakfastQuantity = breakfastQuantity;
        this.lunchQuantity = lunchQuantity;
        this.dinnerQuantity = dinnerQuantity;
    }
}
