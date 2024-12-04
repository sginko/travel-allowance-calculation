/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.domain.travelReport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietEditDto;

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

    @OneToOne(mappedBy = "dietEntity")
    private TravelReportEntity travelReportEntity;

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

    public DietEntity(TravelReportEntity travelReportEntity, BigDecimal dailyAllowance, Integer numberOfBreakfasts,
                      Integer numberOfLunches, Integer numberOfDinners) {
        this.dailyAllowance = dailyAllowance != null ? dailyAllowance : BigDecimal.valueOf(45);
        this.travelReportEntity = travelReportEntity;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
        this.dietAmount = calculateDietAmount();
        this.foodAmount = calculateFoodAmount();
    }

    public BigDecimal calculateTotalDiet() {
        return dietAmount.subtract(foodAmount);
    }

    public void updateDietDetails(DietEditDto dietEditDto) {
        this.dailyAllowance = dietEditDto.getDailyAllowance();
        this.numberOfBreakfasts = dietEditDto.getNumberOfBreakfasts();
        this.numberOfLunches = dietEditDto.getNumberOfLunches();
        this.numberOfDinners = dietEditDto.getNumberOfDinners();

        this.dietAmount = calculateDietAmount();
        this.foodAmount = calculateFoodAmount();
    }

    private BigDecimal calculateDietAmount() {
        long hoursInTravel = getTravelDurationInHours();
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

    private long getTravelDurationInHours() {
        return Duration.between(travelReportEntity.getStartTime().atDate(travelReportEntity.getStartDate()),
                travelReportEntity.getEndTime().atDate(travelReportEntity.getEndDate())).toHours();
    }
}
