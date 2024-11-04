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
    void should_calculate_dietAmount_exactly_8_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(16, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        long duration = dietEntity.getDurationInHours();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(22.5);

        // THEN
        assertThat(duration).isEqualTo(8);
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_dietAmount_less_than_8_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(15, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        long duration = dietEntity.getDurationInHours();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(0);

        // THEN
        assertThat(duration).isEqualTo(7);
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_dietAmount_between_8_and_12_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(17, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        long duration = dietEntity.getDurationInHours();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(22.5);

        // THEN
        assertThat(duration).isEqualTo(9);
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_dietAmount_more_than_12_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(21, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        long duration = dietEntity.getDurationInHours();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(45);

        // THEN
        assertThat(duration).isEqualTo(13);
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_dietAmount_exactly_12_hours() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(20, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        long duration = dietEntity.getDurationInHours();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(45);

        // THEN
        assertThat(duration).isEqualTo(12);
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_dietAmount_multiple_days() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(2), LocalTime.of(10, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        // WHEN
        BigDecimal dietAmount = dietEntity.getDietAmount();
        long duration = dietEntity.getDurationInHours();
        BigDecimal expectedDietAmount = BigDecimal.valueOf(112.5);

        // THEN
        assertThat(duration).isEqualTo(50);
        assertThat(dietAmount).isEqualByComparingTo(expectedDietAmount);
    }

    @Test
    void should_calculate_foodAmount() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 1, 1, 1);

        // WHEN
        BigDecimal foodAmount = dietEntity.getFoodAmount();
        BigDecimal expectedFoodAmount = BigDecimal.valueOf(-45);

        // THEN
        assertThat(foodAmount).isEqualByComparingTo(expectedFoodAmount);
    }

    @Test
    void should_calculate_totalDietAmount() {
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
        assertEquals(expectedTotalDiet, dietAmount.add(foodAmount));
    }
}
