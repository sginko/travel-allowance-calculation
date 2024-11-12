package pl.sginko.travelexpense.logic.travelexpense.diet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.Duration;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "diet")
public class DietEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @Column(nullable = false)
    private BigDecimal dailyAllowance;

    @Min(value = 0, message = "Number of breakfasts cannot be negative")
    @Column(nullable = false)
    private Integer numberOfBreakfasts;

    @Min(value = 0, message = "Number of lunches cannot be negative")
    @Column(nullable = false)
    private Integer numberOfLunches;

    @Min(value = 0, message = "Number of dinners cannot be negative")
    @Column(nullable = false)
    private Integer numberOfDinners;

    @Column(nullable = false)
    private BigDecimal dietAmount;

    @Column(nullable = false)
    private BigDecimal foodAmount;

    public DietEntity(TravelEntity travelEntity, BigDecimal dailyAllowance, Integer numberOfBreakfasts,
                      Integer numberOfLunches, Integer numberOfDinners) {
        this.dailyAllowance = dailyAllowance != null ? dailyAllowance : BigDecimal.valueOf(45);
        this.travelEntity = travelEntity;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
        this.dietAmount = calculateDietAmount();
        this.foodAmount = calculateFoodAmount();
    }

    public BigDecimal calculateDiet() {
        return dietAmount.subtract(foodAmount);
    }

    private BigDecimal calculateDietAmount() {
        long hoursInTravel = getDurationInHours();
        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.50));
        BigDecimal dietAmount = BigDecimal.ZERO;

        if (hoursInTravel <= 24) {
            dietAmount = (hoursInTravel < 8) ? BigDecimal.ZERO :
                    (hoursInTravel <= 12) ? fiftyPercentOfDailyAllowance :
                            dailyAllowance;
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            dietAmount = dietAmount.add(dailyAllowance.multiply(BigDecimal.valueOf(fullDays)));

            if (remainingHours > 0) {
                dietAmount = dietAmount.add((remainingHours <= 8) ? fiftyPercentOfDailyAllowance : dailyAllowance);
            }
        }
        return dietAmount;
    }

    private BigDecimal calculateFoodAmount() {
        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.50));
        BigDecimal twentyFivePercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = twentyFivePercentOfDailyAllowance.multiply(BigDecimal.valueOf(numberOfBreakfasts));
        BigDecimal lunchCost = fiftyPercentOfDailyAllowance.multiply(BigDecimal.valueOf(numberOfLunches));
        BigDecimal dinnerCost = twentyFivePercentOfDailyAllowance.multiply(BigDecimal.valueOf(numberOfDinners));

        return breakfastCost.add(lunchCost).add(dinnerCost);
    }

    private long getDurationInHours() {
        return Duration.between(travelEntity.getStartTime().atDate(travelEntity.getStartDate()),
                travelEntity.getEndTime().atDate(travelEntity.getEndDate())).toHours();
    }
}
