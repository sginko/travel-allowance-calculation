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

import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietEditDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DietEntityTest {
    private static final BigDecimal DAILY_ALLOWANCE = BigDecimal.valueOf(45);
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME_SHORT = LocalTime.of(15, 0);
    private static final LocalTime END_TIME_8_HOURS = LocalTime.of(16, 0);
    private static final LocalTime END_TIME_12_HOURS = LocalTime.of(20, 0);

    @Test
    void should_return_zero_dietAmount_when_travel_duration_is_less_than_8_hours() {
        // GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE, END_TIME_SHORT, null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE,
                0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = BigDecimal.ZERO;

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_return_half_dailyAllowance_when_travel_duration_is_exactly_8_hours() {
        // GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE, END_TIME_8_HOURS, null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE,
                0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.5));

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_return_half_dailyAllowance_when_travel_duration_is_exactly_12_hours() {
        // GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE, END_TIME_12_HOURS, null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE,
                0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.5));

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_dietAmount_for_multiple_days() {
        // GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE.plusDays(2), END_TIME_8_HOURS, null,
                BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE,
                0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(2.5));

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_foodAmount_based_on_provided_meals() {
        // GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE.plusDays(1), LocalTime.of(18, 0), null,
                BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE,
                1, 1, 1);

        // WHEN
        BigDecimal foodAmount = dietEntity.getFoodAmount();
        BigDecimal expectedFoodAmount = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.25))
                .add(DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.5)))
                .add(DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.25)));

        // THEN
        assertThat(foodAmount).isEqualByComparingTo(expectedFoodAmount);
    }

    @Test
    void should_calculate_totalDietAmount_as_difference_between_diet_and_foodAmounts() {
        // GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE.plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE,
                1, 1, 1);

        // WHEN
        BigDecimal expectedTotalDiet = dietEntity.calculateTotalDiet();
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal foodAmount = dietEntity.getFoodAmount();

        // THEN
        assertEquals(expectedTotalDiet, dietAmount.subtract(foodAmount));
    }

    @Test
    void should_return_full_dailyAllowance_when_travel_duration_is_greater_than_12_but_less_than_24_hours() {
        // GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE, LocalTime.of(21, 0), null,
                BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE,
                0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = DAILY_ALLOWANCE;

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_update_dietDetails_based_on_dietEditDto() {
        // GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE.plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE, 1, 1, 1);

        DietEditDto dietEditDto = new DietEditDto(BigDecimal.valueOf(50), 2, 2, 2);

        // WHEN
        dietEntity.updateDietDetails(dietEditDto);

        // THEN
        assertThat(dietEntity.getDailyAllowance()).isEqualByComparingTo(dietEditDto.getDailyAllowance());
        assertThat(dietEntity.getNumberOfBreakfasts()).isEqualTo(dietEditDto.getNumberOfBreakfasts());
        assertThat(dietEntity.getNumberOfLunches()).isEqualTo(dietEditDto.getNumberOfLunches());
        assertThat(dietEntity.getNumberOfDinners()).isEqualTo(dietEditDto.getNumberOfDinners());

        BigDecimal expectedDietAmount = calculateExpectedDietAmount(travelReportEntity, dietEntity.getDailyAllowance());
        BigDecimal expectedFoodAmount = calculateExpectedFoodAmount(dietEntity);

        assertThat(dietEntity.getDietAmount()).isEqualByComparingTo(expectedDietAmount);
        assertThat(dietEntity.getFoodAmount()).isEqualByComparingTo(expectedFoodAmount);
    }

    private BigDecimal calculateExpectedDietAmount(TravelReportEntity travelReportEntity, BigDecimal dailyAllowance) {
        long hoursInTravel = Duration.between(
                travelReportEntity.getStartDate().atTime(travelReportEntity.getStartTime()),
                travelReportEntity.getEndDate().atTime(travelReportEntity.getEndTime())).toHours();

        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.50));

        BigDecimal dietAmount = BigDecimal.ZERO;
        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                dietAmount = BigDecimal.ZERO;
            } else if (hoursInTravel <= 12) {
                dietAmount = fiftyPercentOfDailyAllowance;
            } else {
                dietAmount = dailyAllowance;
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            dietAmount = dailyAllowance.multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours > 0) {
                if (remainingHours <= 8) {
                    dietAmount = dietAmount.add(fiftyPercentOfDailyAllowance); // Исправлено
                } else {
                    dietAmount = dietAmount.add(dailyAllowance);
                }
            }
        }
        return dietAmount;
    }

    private BigDecimal calculateExpectedFoodAmount(DietEntity dietEntity) {
        BigDecimal dailyAllowance = dietEntity.getDailyAllowance();
        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.50));
        BigDecimal twentyFivePercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = twentyFivePercentOfDailyAllowance.multiply(BigDecimal.valueOf(dietEntity.getNumberOfBreakfasts()));
        BigDecimal lunchCost = fiftyPercentOfDailyAllowance.multiply(BigDecimal.valueOf(dietEntity.getNumberOfLunches()));
        BigDecimal dinnerCost = twentyFivePercentOfDailyAllowance.multiply(BigDecimal.valueOf(dietEntity.getNumberOfDinners()));

        return breakfastCost.add(lunchCost).add(dinnerCost);
    }
}
