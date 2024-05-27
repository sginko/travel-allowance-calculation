package pl.sginko.travelallowance.model.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelResponseDto {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer breakfastQuantity;
    private Integer lunchQuantity;
    private Integer dinnerQuantity;
//    private BigDecimal totalAmount;
    private BigDecimal costOfTotalExpense;

    public TravelResponseDto(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer breakfastQuantity,
                             Integer lunchQuantity, Integer dinnerQuantity, BigDecimal costOfTotalExpense) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.breakfastQuantity = breakfastQuantity;
        this.lunchQuantity = lunchQuantity;
        this.dinnerQuantity = dinnerQuantity;
//        this.totalAmount = totalAmount;
        this.costOfTotalExpense = costOfTotalExpense;
    }
}