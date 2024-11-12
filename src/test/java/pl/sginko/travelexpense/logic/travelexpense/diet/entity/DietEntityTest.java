package pl.sginko.travelexpense.logic.travelexpense.diet.entity;

import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
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
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE, END_TIME_SHORT, null, BigDecimal.ZERO, BigDecimal.ZERO);
        DietEntity dietEntity = new DietEntity(travelEntity, DAILY_ALLOWANCE, 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = BigDecimal.ZERO;

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_return_half_dailyAllowance_when_travel_duration_is_exactly_8_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE, END_TIME_8_HOURS, null, BigDecimal.ZERO, BigDecimal.ZERO);
        DietEntity dietEntity = new DietEntity(travelEntity, DAILY_ALLOWANCE, 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.5));

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_return_half_dailyAllowance_when_travel_duration_is_exactly_12_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE, END_TIME_12_HOURS, null, BigDecimal.ZERO, BigDecimal.ZERO);
        DietEntity dietEntity = new DietEntity(travelEntity, DAILY_ALLOWANCE, 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.5));

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_dietAmount_for_multiple_days() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE.plusDays(2), END_TIME_8_HOURS, null,
                BigDecimal.ZERO, BigDecimal.ZERO);
        DietEntity dietEntity = new DietEntity(travelEntity, DAILY_ALLOWANCE, 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(2.5));

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_foodAmount_based_on_provided_meals() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE.plusDays(1), LocalTime.of(18, 0), null,
                BigDecimal.ZERO, BigDecimal.ZERO);
        DietEntity dietEntity = new DietEntity(travelEntity, DAILY_ALLOWANCE, 1, 1, 1);

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
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE.plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);
        DietEntity dietEntity = new DietEntity(travelEntity, DAILY_ALLOWANCE, 1, 1, 1);

        // WHEN
        BigDecimal expectedTotalDiet = dietEntity.calculateDiet();
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal foodAmount = dietEntity.getFoodAmount();

        // THEN
        assertEquals(expectedTotalDiet, dietAmount.subtract(foodAmount));
    }

    @Test
    void should_return_full_dailyAllowance_when_travel_duration_is_greater_than_12_but_less_than_24_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                START_DATE, START_TIME, START_DATE, LocalTime.of(21, 0), null, BigDecimal.ZERO, BigDecimal.ZERO);
        DietEntity dietEntity = new DietEntity(travelEntity, DAILY_ALLOWANCE, 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        BigDecimal expectedDietAmount = DAILY_ALLOWANCE;

        // THEN
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }
}
