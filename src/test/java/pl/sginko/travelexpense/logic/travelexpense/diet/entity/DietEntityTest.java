package pl.sginko.travelexpense.logic.travelexpense.diet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DietEntityTest {
    private TravelEntity mockTravelEntity;
    private BigDecimal dailyAllowance;
    private DietEntity dietEntity;

    @BeforeEach
    void setUp() {
        mockTravelEntity = mock(TravelEntity.class);

        when(mockTravelEntity.getStartDate()).thenReturn(LocalDate.of(2023, 1, 1));
        when(mockTravelEntity.getStartTime()).thenReturn(LocalTime.of(8, 0));
        when(mockTravelEntity.getEndDate()).thenReturn(LocalDate.of(2023, 1, 2));
        when(mockTravelEntity.getEndTime()).thenReturn(LocalTime.of(8, 0));

        dailyAllowance = BigDecimal.valueOf(45);
    }

    @Test
    void testDietAmountCalculationForLessThan8Hours() {
        // Modify mock travel times to be less than 8 hours
        when(mockTravelEntity.getEndDate()).thenReturn(LocalDate.of(2023, 1, 1));
        when(mockTravelEntity.getEndTime()).thenReturn(LocalTime.of(15, 0));

        dietEntity = new DietEntity(mockTravelEntity, dailyAllowance, 0, 0, 0);

        assertEquals(BigDecimal.ZERO, dietEntity.calculateDiet(), "Diet amount should be zero for less than 8 hours");
    }

    @Test
    void testDietAmountCalculationFor8To12Hours() {
        // Modify mock travel times to be between 8 and 12 hours
        when(mockTravelEntity.getEndDate()).thenReturn(LocalDate.of(2023, 1, 1));
        when(mockTravelEntity.getEndTime()).thenReturn(LocalTime.of(20, 0));

        dietEntity = new DietEntity(mockTravelEntity, dailyAllowance, 0, 0, 0);

        BigDecimal expectedAmount = dailyAllowance.multiply(BigDecimal.valueOf(0.50));
        assertEquals(expectedAmount, dietEntity.calculateDiet(), "Diet amount should be 50% of daily allowance for 8 to 12 hours");
    }

    @Test
    void testDietAmountCalculationForMoreThan12Hours() {
        // Modify mock travel times to be more than 12 hours but less than 24 hours
        when(mockTravelEntity.getEndDate()).thenReturn(LocalDate.of(2023, 1, 2));
        when(mockTravelEntity.getEndTime()).thenReturn(LocalTime.of(1, 0));

        dietEntity = new DietEntity(mockTravelEntity, dailyAllowance, 0, 0, 0);

        assertEquals(dailyAllowance, dietEntity.calculateDiet(), "Diet amount should be the full daily allowance for more than 12 hours");
    }

    @Test
    void testDietAmountCalculationForMultipleDays() {
        // Modify mock travel times to be more than 24 hours
        when(mockTravelEntity.getEndDate()).thenReturn(LocalDate.of(2023, 1, 3));
        when(mockTravelEntity.getEndTime()).thenReturn(LocalTime.of(10, 0));

        dietEntity = new DietEntity(mockTravelEntity, dailyAllowance, 0, 0, 0);

        BigDecimal expectedAmount = dailyAllowance.multiply(BigDecimal.valueOf(2.5));
        assertEquals(expectedAmount, dietEntity.calculateDiet(), "Diet amount should be correct for multiple days and remaining hours");
    }

    @Test
    void testFoodAmountCalculation() {
        dietEntity = new DietEntity(mockTravelEntity, dailyAllowance, 2, 1, 1);

        BigDecimal twentyFivePercent = dailyAllowance.multiply(BigDecimal.valueOf(0.25));
        BigDecimal fiftyPercent = dailyAllowance.multiply(BigDecimal.valueOf(0.50));

        BigDecimal expectedFoodAmount = twentyFivePercent.multiply(BigDecimal.valueOf(2))
                .add(fiftyPercent.multiply(BigDecimal.valueOf(1)))
                .add(twentyFivePercent.multiply(BigDecimal.valueOf(1)))
                .negate();

//        assertEquals(expectedFoodAmount, dietEntity.calculateDiet().subtract(dietEntity.calculateDietAmount()), "Food amount should be correctly calculated and negated");
    }

    @Test
    void testDietEntityWithDefaults() {
        dietEntity = new DietEntity(mockTravelEntity, null, 0, 0, 0);

        assertEquals(BigDecimal.valueOf(45), dietEntity.getDailyAllowance(), "Daily allowance should default to 45 if null is provided");
    }
}