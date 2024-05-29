package pl.sginko.travelallowance.model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)

@Entity
public class TravelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime startTravel;
    private LocalDateTime finishTravel;
    private BigDecimal dailyAllowance;

    private Integer breakfastQuantity;
    private Integer lunchQuantity;
    private Integer dinnerQuantity;

    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal dietAmount = BigDecimal.ZERO;
    private BigDecimal foodAmount = BigDecimal.ZERO;

    public TravelEntity(LocalDateTime startTravel, LocalDateTime finishTravel, BigDecimal dailyAllowance,
                        Integer breakfastQuantity, Integer lunchQuantity, Integer dinnerQuantity) {
        this.startTravel = startTravel;
        this.finishTravel = finishTravel;
        this.dailyAllowance = dailyAllowance;
        this.breakfastQuantity = breakfastQuantity;
        this.lunchQuantity = lunchQuantity;
        this.dinnerQuantity = dinnerQuantity;

    }

    public void calculateDiet() {
        long hoursInTravel = Duration.between(startTravel, finishTravel).toHours();
        BigDecimal fiftyPercentOfDailyAllowance = BigDecimal.valueOf(50 / 100.0).multiply(dailyAllowance);

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                this.dietAmount = new BigDecimal(0);
            }
            if (hoursInTravel < 12) {
                this.dietAmount = fiftyPercentOfDailyAllowance;
            }
            this.dietAmount = dailyAllowance;
        } else {
            long hoursInTravelLessThanDay = hoursInTravel % 24;
            long fullDays = Duration.between(startTravel, finishTravel).toDays();
            BigDecimal totalAmountForFullDays = BigDecimal.valueOf(fullDays).multiply(dailyAllowance);

            if (hoursInTravelLessThanDay < 8) {
                this.dietAmount = totalAmountForFullDays.add(fiftyPercentOfDailyAllowance);
            }
            this.dietAmount = totalAmountForFullDays.add(dailyAllowance);
        }
        recalculateTotalAmount();
    }

    public void calculateFood() {
        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = BigDecimal.valueOf(50 / 100.0).multiply(dailyAllowance);
        BigDecimal twentyFivePercentOfDailyAllowance = BigDecimal.valueOf(25 / 100.0).multiply(dailyAllowance);

        BigDecimal moneyCostForBreakfast = BigDecimal.valueOf(breakfastQuantity).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal moneyCostForLunch = BigDecimal.valueOf(lunchQuantity).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal moneyCostForDinner = BigDecimal.valueOf(dinnerQuantity).multiply(twentyFivePercentOfDailyAllowance);
        this.foodAmount = totalFoodExpenses.add(moneyCostForBreakfast).add(moneyCostForLunch).add(moneyCostForDinner).multiply(new BigDecimal(-1));
        recalculateTotalAmount();
    }

    private void recalculateTotalAmount() {
        this.totalAmount = this.dietAmount.add(this.foodAmount);
    }
}