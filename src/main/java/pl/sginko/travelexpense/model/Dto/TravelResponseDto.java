package pl.sginko.travelexpense.model.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelResponseDto {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer breakfastQuantity;
    private Integer lunchQuantity;
    private Integer dinnerQuantity;
    private BigDecimal totalAmount;
    private BigDecimal dietAmount;
    private BigDecimal foodAmount;

    public TravelResponseDto(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer breakfastQuantity,
                             Integer lunchQuantity, Integer dinnerQuantity, BigDecimal totalAmount,
                             BigDecimal dietAmount, BigDecimal foodAmount) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.breakfastQuantity = breakfastQuantity;
        this.lunchQuantity = lunchQuantity;
        this.dinnerQuantity = dinnerQuantity;
        this.totalAmount = totalAmount;
        this.dietAmount = dietAmount;
        this.foodAmount = foodAmount;
    }
}