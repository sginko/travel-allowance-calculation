package pl.sginko.travelexpense.logic.travelexpense.diet.entity;

import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DietEntityTest {
    @Test
    void should_return_zero_dietAmount_when_travel_duration_is_less_than_8_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(15, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

//        // WHEN
//        BigDecimal dietAmount = dietEntity.getDietAmount();
//        long duration = dietEntity.getDurationInHours();
//        BigDecimal expectedDietAmount = BigDecimal.valueOf(0);
//
//        // THEN
//        assertThat(duration).isEqualTo(7);
//        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(0);

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_return_half_daily_allowance_when_travel_duration_is_exactly_8_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(16, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(22.5);

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_return_half_daily_allowance_when_travel_duration_is_between_8_and_12_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(17, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(22.5);

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_return_full_daily_allowance_when_travel_duration_is_exactly_12_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(20, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(45);

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_return_full_daily_allowance_when_travel_duration_is_more_than_12_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(21, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(45);

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_dietAmount_for_multiple_days() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(2), LocalTime.of(10, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(112.5);

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_return_correct_dietAmount_when_remainingHours_is_zero() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(2), LocalTime.of(8, 0), // Точно 48 часов (2 полных дня)
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(90);

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_foodAmount_based_on_provided_meals() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 1, 1, 1);

        // WHEN
        BigDecimal foodAmount = dietEntity.getFoodAmount();
        BigDecimal expectedFoodAmount = BigDecimal.valueOf(45);

        // THEN
        assertThat(foodAmount).isEqualByComparingTo(expectedFoodAmount);
    }

    @Test
    void should_calculate_foodAmount_for_different_meal_combinations() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 2, 1, 1);

        // WHEN
        BigDecimal foodAmount = dietEntity.getFoodAmount();
        BigDecimal expectedFoodAmount = BigDecimal.valueOf(22.5 + 22.5 + 11.25);

        // THEN
        assertThat(foodAmount).isEqualByComparingTo(expectedFoodAmount);
    }

    @Test
    void should_calculate_totalDietAmount_as_difference_between_diet_and_foodAmounts() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 1, 1, 1);

        // WHEN
        BigDecimal expectedTotalDiet = dietEntity.calculateDiet();
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal foodAmount = dietEntity.getFoodAmount();

        // THEN
        assertEquals(expectedTotalDiet, dietAmount.subtract(foodAmount));
    }
}
