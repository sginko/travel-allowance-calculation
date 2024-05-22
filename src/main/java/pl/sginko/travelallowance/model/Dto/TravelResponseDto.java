package pl.sginko.travelallowance.model.Dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelResponseDto {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer breakfastQuantity;
    private Integer lunchQuantity;
    private Integer dinnerQuantity;
    private BigDecimal totalAmount;


    public TravelResponseDto(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer breakfastQuantity,
                             Integer lunchQuantity, Integer dinnerQuantity, BigDecimal totalAmount) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.breakfastQuantity = breakfastQuantity;
        this.lunchQuantity = lunchQuantity;
        this.dinnerQuantity = dinnerQuantity;
        this.totalAmount = totalAmount;
    }
}
