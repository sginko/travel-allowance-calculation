package pl.sginko.travelexpense.model.diet.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
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
    private BigDecimal dailyAllowance = BigDecimal.valueOf(45);

    @Column(nullable = false)
    private BigDecimal dietAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer numberOfBreakfasts;

    @Column(nullable = false)
    private Integer numberOfLunches;

    @Column(nullable = false)
    private Integer numberOfDinners;

    public DietEntity(TravelEntity travelEntity, Integer numberOfBreakfasts, Integer numberOfLunches, Integer numberOfDinners) {
        this.travelEntity = travelEntity;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
        calculateDietAmount();
        calculateFoodAmount();
    }

    public void calculateDietAmount() {
        LocalDate startDate = travelEntity.getStartDate();
        LocalTime startTime = travelEntity.getStartTime();
        LocalDate endDate = travelEntity.getEndDate();
        LocalTime endTime = travelEntity.getEndTime();
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

            if (remainingHours == 0) {
                this.dietAmount = totalAmountForFullDays.add(BigDecimal.ZERO);
            } else if (remainingHours < 8) {
                this.dietAmount = totalAmountForFullDays.add(fiftyPercentOfDailyAllowance);
            } else {
                this.dietAmount = totalAmountForFullDays.add(dailyAllowance);
            }
        }
    }

    public void calculateFoodAmount() {
        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(numberOfBreakfasts).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(numberOfLunches).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(numberOfDinners).multiply(twentyFivePercentOfDailyAllowance);

        this.dietAmount = this.dietAmount.subtract(breakfastCost).subtract(lunchCost).subtract(dinnerCost);
    }
}
