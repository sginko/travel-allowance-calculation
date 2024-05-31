package pl.sginko.travelexpense.model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)

@Entity
public class TravelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private BigDecimal dailyAllowance;

    private Integer numberOfBreakfasts;
    private Integer numberOfLunches;
    private Integer numberOfDinners;

    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal dietAmount = BigDecimal.ZERO;
    private BigDecimal foodAmount = BigDecimal.ZERO;

    public TravelEntity(LocalDate startDate, LocalTime startTime, LocalDate endDate,
                        LocalTime endTime, BigDecimal dailyAllowance, Integer numberOfBreakfasts,
                        Integer numberOfLunches, Integer numberOfDinners) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.dailyAllowance = dailyAllowance;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
    }

    public void calculateDietAmount() {
        long hoursInTravel = Duration.between(startTime.atDate(startDate), endTime.atDate(endDate)).toHours();
        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.50));

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                this.dietAmount = BigDecimal.ZERO;
            } else if (hoursInTravel < 12) {
                this.dietAmount = fiftyPercentOfDailyAllowance;
            } else {
                this.dietAmount = dailyAllowance;
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            BigDecimal totalAmountForFullDays = dailyAllowance.multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours < 8) {
                this.dietAmount = totalAmountForFullDays.add(fiftyPercentOfDailyAllowance);
            } else {
                this.dietAmount = totalAmountForFullDays.add(dailyAllowance);
            }
        }
        updateTotalAmount();
    }

//    public void calculateDietAmount() {
//        long hoursInTravel = Duration.between(startTime.atDate(startDate), endTime.atDate(endDate)).toHours();
//        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.50));
//
//        if (hoursInTravel <= 24) {
//            if (hoursInTravel < 8) {
//                this.dietAmount = BigDecimal.ZERO;
//            }
//            if (hoursInTravel < 12) {
//                this.dietAmount = fiftyPercentOfDailyAllowance;
//            }
//            this.dietAmount = dailyAllowance;
//        } else {
//            long fullDays = hoursInTravel / 24;
//            long remainingHours = hoursInTravel % 24;
//            BigDecimal totalAmountForFullDays = dailyAllowance.multiply(BigDecimal.valueOf(fullDays));
//
//            if (remainingHours < 8) {
//                this.dietAmount = totalAmountForFullDays.add(fiftyPercentOfDailyAllowance);
//            }
//            this.dietAmount = totalAmountForFullDays.add(dailyAllowance);
//        }
//        updateTotalAmount();
//    }

    public void calculateFoodAmount() {
        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(numberOfBreakfasts).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(numberOfLunches).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(numberOfDinners).multiply(twentyFivePercentOfDailyAllowance);

        this.foodAmount = totalFoodExpenses.add(breakfastCost).add(lunchCost).add(dinnerCost).negate();
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount = this.dietAmount.add(this.foodAmount);
    }
}