package pl.sginko.travelexpense.logic.travelexpense.diet.entity;

import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DietEntityTest {
    @Test
    void should_calculate_diet_amount_for_short_travel() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity();
        travelEntity.setDurationInHours(10); // Например, поездка длится 10 часов

        BigDecimal dailyAllowance = BigDecimal.valueOf(100); // Суточные 100
        DietEntity dietEntity = new DietEntity(travelEntity, dailyAllowance, 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.calculateDietAmount();

        // THEN
        BigDecimal expectedAmount = dailyAllowance.multiply(BigDecimal.valueOf(0.50));
        assertThat(dietAmount).isEqualByComparingTo(expectedAmount);
    }

    @Test
    void should_calculate_diet_amount_for_full_day_travel() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity();
        travelEntity.setDurationInHours(24); // Полный день (24 часа)

        BigDecimal dailyAllowance = BigDecimal.valueOf(100); // Суточные 100
        DietEntity dietEntity = new DietEntity(travelEntity, dailyAllowance, 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.calculateDietAmount();

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(dailyAllowance);
    }

    @Test
    void should_calculate_food_amount_correctly() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity();
        travelEntity.setDurationInHours(24); // Полный день

        BigDecimal dailyAllowance = BigDecimal.valueOf(100); // Суточные 100
        int breakfasts = 1;
        int lunches = 1;
        int dinners = 1;
        DietEntity dietEntity = new DietEntity(travelEntity, dailyAllowance, breakfasts, lunches, dinners);

        // WHEN
        BigDecimal foodAmount = dietEntity.calculateFoodAmount();

        // THEN
        BigDecimal expectedBreakfastCost = dailyAllowance.multiply(BigDecimal.valueOf(0.25)).multiply(BigDecimal.valueOf(breakfasts));
        BigDecimal expectedLunchCost = dailyAllowance.multiply(BigDecimal.valueOf(0.50)).multiply(BigDecimal.valueOf(lunches));
        BigDecimal expectedDinnerCost = dailyAllowance.multiply(BigDecimal.valueOf(0.25)).multiply(BigDecimal.valueOf(dinners));
        BigDecimal expectedFoodAmount = expectedBreakfastCost.add(expectedLunchCost).add(expectedDinnerCost).negate();

        assertThat(foodAmount).isEqualByComparingTo(expectedFoodAmount);
    }

    @Test
    void should_calculate_total_diet_amount() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity();
        travelEntity.setDurationInHours(24); // Полный день

        BigDecimal dailyAllowance = BigDecimal.valueOf(100); // Суточные 100
        int breakfasts = 1;
        int lunches = 1;
        int dinners = 1;
        DietEntity dietEntity = new DietEntity(travelEntity, dailyAllowance, breakfasts, lunches, dinners);

        // WHEN
        BigDecimal totalDiet = dietEntity.calculateDiet();

        // THEN
        BigDecimal dietAmount = dietEntity.calculateDietAmount();
        BigDecimal foodAmount = dietEntity.calculateFoodAmount();
        BigDecimal expectedTotalDiet = dietAmount.add(foodAmount);

        assertThat(totalDiet).isEqualByComparingTo(expectedTotalDiet);
    }


// ------------------------
//    @Test
//    void should_calculate_food_amount_when_breakfast_is_0_lunch_is_0_dinner_is_0() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//        when(dietDto.getNumberOfBreakfasts()).thenReturn(0);
//        when(dietDto.getNumberOfLunches()).thenReturn(0);
//        when(dietDto.getNumberOfDinners()).thenReturn(0);
//
//        //WHEN
//        BigDecimal foodAmount = dietService.calculateFoodAmount(travelRequestDto);
//
//        //THEN
//        assertThat(foodAmount).isEqualByComparingTo(BigDecimal.valueOf(0.0));
//        assertTrue(foodAmount.compareTo(BigDecimal.valueOf(0.0)) == 0);
//    }
//
//    @Test
//    void should_calculate_food_amount_when_breakfast_is_1_lunch_is_0_dinner_is_0() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//        when(dietDto.getNumberOfBreakfasts()).thenReturn(1);
//        when(dietDto.getNumberOfLunches()).thenReturn(0);
//        when(dietDto.getNumberOfDinners()).thenReturn(0);
//
//        //WHEN
//        BigDecimal foodAmount = dietService.calculateFoodAmount(travelRequestDto);
//
//        //THEN
//        assertThat(foodAmount).isEqualByComparingTo(BigDecimal.valueOf(-11.25));
//        assertTrue(foodAmount.compareTo(BigDecimal.valueOf(-11.25)) == 0);
//    }
//
//    @Test
//    void should_calculate_food_amount_when_breakfast_is_0_lunch_is_1_dinner_is_0() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//        when(dietDto.getNumberOfBreakfasts()).thenReturn(0);
//        when(dietDto.getNumberOfLunches()).thenReturn(1);
//        when(dietDto.getNumberOfDinners()).thenReturn(0);
//
//        //WHEN
//        BigDecimal foodAmount = dietService.calculateFoodAmount(travelRequestDto);
//
//        //THEN
//        assertThat(foodAmount).isEqualByComparingTo(BigDecimal.valueOf(-22.5));
//        assertTrue(foodAmount.compareTo(BigDecimal.valueOf(-22.50)) == 0);
//    }
//
//    @Test
//    void should_calculate_food_amount_when_breakfast_is_0_lunch_is_0_dinner_is_1() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//        when(dietDto.getNumberOfBreakfasts()).thenReturn(1);
//        when(dietDto.getNumberOfLunches()).thenReturn(0);
//        when(dietDto.getNumberOfDinners()).thenReturn(0);
//
//        //WHEN
//        BigDecimal foodAmount = dietService.calculateFoodAmount(travelRequestDto);
//
//        //THEN
//        assertThat(foodAmount).isEqualByComparingTo(BigDecimal.valueOf(-11.25));
//        assertTrue(foodAmount.compareTo(BigDecimal.valueOf(-11.25)) == 0);
//    }
//
//    @Test
//    void should_calculate_food_amount_when_breakfast_is_1_lunch_is_0_dinner_is_1() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//        when(dietDto.getNumberOfBreakfasts()).thenReturn(0);
//        when(dietDto.getNumberOfLunches()).thenReturn(1);
//        when(dietDto.getNumberOfDinners()).thenReturn(0);
//
//        //WHEN
//        BigDecimal foodAmount = dietService.calculateFoodAmount(travelRequestDto);
//
//        //THEN
//        assertThat(foodAmount).isEqualByComparingTo(BigDecimal.valueOf(-22.5));
//        assertTrue(foodAmount.compareTo(BigDecimal.valueOf(-22.5)) == 0);
//    }
//
//    @Test
//    void should_calculate_food_amount_when_breakfast_is_1_lunch_is_1_dinner_is_1() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//        when(dietDto.getNumberOfBreakfasts()).thenReturn(0);
//        when(dietDto.getNumberOfLunches()).thenReturn(1);
//        when(dietDto.getNumberOfDinners()).thenReturn(0);
//
//        //WHEN
//        BigDecimal foodAmount = dietService.calculateFoodAmount(travelRequestDto);
//
//        //THEN
//        assertThat(foodAmount).isEqualByComparingTo(BigDecimal.valueOf(-22.5));
//        assertTrue(foodAmount.compareTo(BigDecimal.valueOf(-22.5)) == 0);
//    }
//
//    @Test
//    void should_calculate_diet_amount_when_trip_less_than_8_hours() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//
//        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
//        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
//        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now());
//        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(7, 59));
//
//        //WHEN
//        BigDecimal dietAmount = dietService.calculateDietAmount(travelRequestDto);
//
//        //THEN
//        assertThat(dietAmount).isEqualByComparingTo(BigDecimal.valueOf(0.0));
//    }
//
//    @Test
//    void should_calculate_diet_amount_when_trip_more_8_and_less_12_hour() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//
//        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
//        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
//        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now());
//        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(11, 59));
//
//        //WHEN
//        BigDecimal dietAmount = dietService.calculateDietAmount(travelRequestDto);
//
//        //THEN
//        assertThat(dietAmount).isEqualByComparingTo(BigDecimal.valueOf(22.5));
//    }
//
//    @Test
//    void should_calculate_diet_amount_when_trip_more_than_12_hours() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//
//        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
//        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(9, 0));
//        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now());
//        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(23, 59));
//
//        //WHEN
//        BigDecimal dietAmount = dietService.calculateDietAmount(travelRequestDto);
//
//        //THEN
//        assertThat(dietAmount).isEqualByComparingTo(BigDecimal.valueOf(45.0));
//    }
//
//    @Test
//    void should_calculate_diet_amount_when_trip_more_than_one_day_and_less_than_8_hours() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//
//        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
//        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
//        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
//        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(7, 59));
//
//        //WHEN
//        BigDecimal dietAmount = dietService.calculateDietAmount(travelRequestDto);
//
//        //THEN
//        assertThat(dietAmount).isEqualByComparingTo(BigDecimal.valueOf(67.5));
//    }
//
//    @Test
//    void should_calculate_diet_amount_when_trip_more_than_one_day_and_more_than_8_hours() {
//        //GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//
//        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
//        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
//        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
//        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(8, 00));
//
//        //WHEN
//        BigDecimal dietAmount = dietService.calculateDietAmount(travelRequestDto);
//
//        //THEN
//        assertThat(dietAmount).isEqualByComparingTo(BigDecimal.valueOf(90.0));
//    }
//
//    @Test
//    void should_calculate_diet() {
//        // GIVEN
//        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
//        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);
//        when(dietDto.getNumberOfBreakfasts()).thenReturn(1);
//        when(dietDto.getNumberOfLunches()).thenReturn(1);
//        when(dietDto.getNumberOfDinners()).thenReturn(1);
//
//        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
//        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
//        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
//        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(8, 0));
//
//        // WHEN
//        BigDecimal diet = dietService.calculateDiet(travelRequestDto);
//
//        // THEN
//        assertThat(diet).isEqualByComparingTo(BigDecimal.valueOf(45).add(BigDecimal.valueOf(45)).add(BigDecimal.valueOf(-45)));
//    }

}