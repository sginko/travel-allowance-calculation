package pl.sginko.travelexpense.logic.travelexpense.transportCost.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.exception.TransportException;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransportCostEntityTest {
    private TravelEntity travelEntity;
    private DietEntity dietEntity;

    @BeforeEach
    void setUp() {
        // Инициализируем базовые объекты для тестов
        travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);
        dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        travelEntity.setDietDetails(dietEntity);
    }

    @Test
    void should_calculate_cost_of_travel_by_own_transport() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelEntity,
                0, BigDecimal.ZERO, "", BigDecimal.ZERO,
                100L, 200L, 50L, 30L);

        // WHEN
        BigDecimal actualCost = transportCostEntity.getCostOfTravelByOwnTransport();

        // THEN
        BigDecimal expectedCost = BigDecimal.valueOf((0.89 * 100) + (1.15 * 200) + (0.69 * 50) + (0.42 * 30));
        assertThat(actualCost).isEqualByComparingTo(expectedCost);
    }

    @Test
    void should_calculate_undocumented_local_transport_cost() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelEntity,
                2, BigDecimal.ZERO, "", BigDecimal.ZERO,
                0L, 0L, 0L, 0L);

        // WHEN
        BigDecimal actualCost = transportCostEntity.getUndocumentedLocalTransportCost();

        // THEN
        BigDecimal dailyAllowance = BigDecimal.valueOf(45);
        BigDecimal expectedCost = dailyAllowance.multiply(BigDecimal.valueOf(0.20)).multiply(BigDecimal.valueOf(2));
        assertThat(actualCost).isEqualByComparingTo(expectedCost);
    }

    @Test
    void should_calculate_transport_cost_amount_with_documented_local_transport() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelEntity,
                1, BigDecimal.valueOf(100), "", BigDecimal.valueOf(200),
                0L, 0L, 0L, 0L);

        // WHEN
        BigDecimal actualTotal = transportCostEntity.getTransportCostAmount();

        // THEN
        BigDecimal expectedTotal = BigDecimal.valueOf(100 + 200);
        assertThat(actualTotal).isEqualByComparingTo(expectedTotal);
    }

    @Test
    void should_calculate_transport_cost_amount_with_undocumented_local_transport() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelEntity,
                1, BigDecimal.ZERO, "", BigDecimal.valueOf(200),
                0L, 0L, 0L, 0L);

        // WHEN
        BigDecimal actualTotal = transportCostEntity.getTransportCostAmount();

        // THEN
        BigDecimal expectedUndocumentedLocalTransportCost = BigDecimal.valueOf(45 * 0.20 * 1);
        BigDecimal expectedTotal = expectedUndocumentedLocalTransportCost.add(BigDecimal.valueOf(200));
        assertThat(actualTotal).isEqualByComparingTo(expectedTotal);
    }

    @Test
    void should_throw_exception_when_days_for_undocumented_cost_exceeds_days_in_travel() {
        // WHEN
        Executable e = () -> new TransportCostEntity(travelEntity,
                3, BigDecimal.ZERO, "", BigDecimal.ZERO,
                0L, 0L, 0L, 0L);

        // THEN
        assertThrows(TransportException.class, e);
    }
}
