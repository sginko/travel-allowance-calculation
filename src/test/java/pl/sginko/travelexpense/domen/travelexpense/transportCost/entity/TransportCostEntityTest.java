package pl.sginko.travelexpense.domen.travelexpense.transportCost.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.sginko.travelexpense.domen.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.domen.travelexpense.transportCost.exception.TransportException;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TransportCostEntityTest {
    @Test
    void should_calculate_cost_of_travel_by_own_transport() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now(), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelEntity.updateDietEntity(dietEntity);

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
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelEntity.updateDietEntity(dietEntity);

        TransportCostEntity transportCostEntity = new TransportCostEntity(travelEntity,
                2, BigDecimal.ZERO, "", BigDecimal.ZERO,
                0L, 0L, 0L, 0L);

        // WHEN
        BigDecimal actualCost = transportCostEntity.getUndocumentedLocalTransportCost();

        // THEN
        BigDecimal expectedCost = BigDecimal.valueOf(45 * 0.20 * 2);
        assertThat(actualCost).isEqualByComparingTo(expectedCost);
    }

    @Test
    void should_calculate_transport_cost_amount_with_documented_local_transport() {
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

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
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelEntity.updateDietEntity(dietEntity);

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
        // GIVEN
        TravelEntity travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelEntity.updateDietEntity(dietEntity);

        // WHEN
        Executable e = () -> new TransportCostEntity(travelEntity,
                3, BigDecimal.ZERO, "", BigDecimal.ZERO,
                0L, 0L, 0L, 0L);

        // THEN
        assertThrows(TransportException.class, e);
    }
}
