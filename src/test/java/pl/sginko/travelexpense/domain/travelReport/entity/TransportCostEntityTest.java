package pl.sginko.travelexpense.domain.travelReport.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostEditDto;
import pl.sginko.travelexpense.domain.travelReport.exception.TransportException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransportCostEntityTest {
    private static final BigDecimal DAILY_ALLOWANCE = BigDecimal.valueOf(45);
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalDate END_DATE = START_DATE.plusDays(1);
    private static final LocalTime END_TIME = LocalTime.of(18, 0);

    private TravelReportEntity travelReportEntity;
    private DietEntity dietEntity;

    @BeforeEach
    void setUp() {
        travelReportEntity = new TravelReportEntity("CityA", "CityB", START_DATE, START_TIME,
                END_DATE, END_TIME, null, BigDecimal.ZERO, BigDecimal.ZERO);

        dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE, 0, 0, 0);

        travelReportEntity.setDietDetails(dietEntity);
    }

    @Test
    void should_calculate_cost_of_travel_by_own_transport() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelReportEntity,
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
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelReportEntity,
                2, BigDecimal.ZERO, "", BigDecimal.ZERO,
                0L, 0L, 0L, 0L);

        // WHEN
        BigDecimal actualCost = transportCostEntity.getUndocumentedLocalTransportCost();

        // THEN
        BigDecimal expectedCost = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.20)).multiply(BigDecimal.valueOf(2));
        assertThat(actualCost).isEqualByComparingTo(expectedCost);
    }

    @Test
    void should_calculate_transport_cost_amount_with_documented_local_transport() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelReportEntity,
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
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelReportEntity,
                1, BigDecimal.ZERO, "", BigDecimal.valueOf(200),
                0L, 0L, 0L, 0L);

        // WHEN
        BigDecimal actualTotal = transportCostEntity.getTransportCostAmount();

        // THEN
        BigDecimal expectedUndocumentedLocalTransportCost = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.20));
        BigDecimal expectedTotal = expectedUndocumentedLocalTransportCost.add(BigDecimal.valueOf(200));
        assertThat(actualTotal).isEqualByComparingTo(expectedTotal);
    }

    @Test
    void should_throw_exception_when_days_for_undocumented_cost_exceeds_days_in_travel() {
        // WHEN
        Executable e = () -> new TransportCostEntity(travelReportEntity, 3,
                BigDecimal.ZERO, "", BigDecimal.ZERO, 0L,
                0L, 0L, 0L);

        // THEN
        assertThrows(TransportException.class, e);
    }

    @Test
    void should_update_transportCostDetails_based_on_transportCostEditDto() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelReportEntity,
                1, BigDecimal.valueOf(100), "Initial transport details",
                BigDecimal.valueOf(200), 100L, 200L,
                50L, 30L);

        TransportCostEditDto transportCostEditDto = new TransportCostEditDto(2,
                BigDecimal.valueOf(150), "Updated transport details", BigDecimal.valueOf(250),
                120L, 210L, 60L, 35L);

        // WHEN
        transportCostEntity.updateTransportCostDetails(transportCostEditDto);

        // THEN
        assertThat(transportCostEntity.getDaysForUndocumentedLocalTransportCost())
                .isEqualTo(transportCostEditDto.getDaysForUndocumentedLocalTransportCost());
        assertThat(transportCostEntity.getDocumentedLocalTransportCost())
                .isEqualByComparingTo(transportCostEditDto.getDocumentedLocalTransportCost());
        assertThat(transportCostEntity.getMeansOfTransport())
                .isEqualTo(transportCostEditDto.getMeansOfTransport());
        assertThat(transportCostEntity.getCostOfTravelByPublicTransport())
                .isEqualByComparingTo(transportCostEditDto.getCostOfTravelByPublicTransport());

        assertThat(transportCostEntity.getKilometersByCarEngineUpTo900cc())
                .isEqualTo(transportCostEditDto.getKilometersByCarEngineUpTo900cc());
        assertThat(transportCostEntity.getKilometersByCarEngineAbove900cc())
                .isEqualTo(transportCostEditDto.getKilometersByCarEngineAbove900cc());
        assertThat(transportCostEntity.getKilometersByMotorcycle())
                .isEqualTo(transportCostEditDto.getKilometersByMotorcycle());
        assertThat(transportCostEntity.getKilometersByMoped())
                .isEqualTo(transportCostEditDto.getKilometersByMoped());

        BigDecimal expectedCostOfTravelByOwnTransport = calculateExpectedCostOfTravelByOwnTransport(transportCostEntity);
        BigDecimal expectedUndocumentedLocalTransportCost = calculateExpectedUndocumentedLocalTransportCost(transportCostEntity);
        BigDecimal expectedTransportCostAmount = expectedUndocumentedLocalTransportCost.add(transportCostEntity.getDocumentedLocalTransportCost())
                .add(transportCostEntity.getCostOfTravelByPublicTransport())
                .add(expectedCostOfTravelByOwnTransport);
        BigDecimal expectedTotalCostOfTravelByOwnAndPublicTransport = transportCostEntity.getCostOfTravelByPublicTransport()
                .add(expectedCostOfTravelByOwnTransport);

        // Проверяем пересчитанные поля
        assertThat(transportCostEntity.getCostOfTravelByOwnTransport())
                .isEqualByComparingTo(expectedCostOfTravelByOwnTransport);
        assertThat(transportCostEntity.getUndocumentedLocalTransportCost())
                .isEqualByComparingTo(expectedUndocumentedLocalTransportCost);
        assertThat(transportCostEntity.getTransportCostAmount())
                .isEqualByComparingTo(expectedTransportCostAmount);
        assertThat(transportCostEntity.getTotalCostOfTravelByOwnAndPublicTransport())
                .isEqualByComparingTo(expectedTotalCostOfTravelByOwnAndPublicTransport);
    }

    private BigDecimal calculateExpectedCostOfTravelByOwnTransport(TransportCostEntity transportCostEntity) {
        BigDecimal amountCostByCarEngineUpTo900Cc = BigDecimal.valueOf(0.89)
                .multiply(BigDecimal.valueOf(transportCostEntity.getKilometersByCarEngineUpTo900cc()));
        BigDecimal amountCostByCarEngineAbove900Cc = BigDecimal.valueOf(1.15)
                .multiply(BigDecimal.valueOf(transportCostEntity.getKilometersByCarEngineAbove900cc()));
        BigDecimal amountCostByMotorcycle = BigDecimal.valueOf(0.69)
                .multiply(BigDecimal.valueOf(transportCostEntity.getKilometersByMotorcycle()));
        BigDecimal amountCostByMoped = BigDecimal.valueOf(0.42)
                .multiply(BigDecimal.valueOf(transportCostEntity.getKilometersByMoped()));

        return amountCostByCarEngineUpTo900Cc.add(amountCostByCarEngineAbove900Cc)
                .add(amountCostByMotorcycle).add(amountCostByMoped);
    }

    private BigDecimal calculateExpectedUndocumentedLocalTransportCost(TransportCostEntity transportCostEntity) {
        if (transportCostEntity.getDocumentedLocalTransportCost().compareTo(BigDecimal.ZERO) > 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal dailyAllowance = transportCostEntity.getTravelReportEntity().getDietEntity().getDailyAllowance();
        BigDecimal dailyUndocumentedLocalTransportCost = dailyAllowance.multiply(BigDecimal.valueOf(0.20));
        long daysInTravel = getDaysInTravel(transportCostEntity);

        if (transportCostEntity.getDaysForUndocumentedLocalTransportCost() > daysInTravel) {
            throw new TransportException("The number of days entered for undocumented Local Transport Costs is greater than the number of days on the trip");
        }

        return dailyUndocumentedLocalTransportCost.multiply(BigDecimal.valueOf(transportCostEntity.getDaysForUndocumentedLocalTransportCost()));
    }

    private long getDaysInTravel(TransportCostEntity transportCostEntity) {
        long hoursInTravel = Duration.between(
                transportCostEntity.getTravelReportEntity().getStartDate().atTime(transportCostEntity.getTravelReportEntity().getStartTime()),
                transportCostEntity.getTravelReportEntity().getEndDate().atTime(transportCostEntity.getTravelReportEntity().getEndTime())
        ).toHours();

        long daysInTravel = hoursInTravel / 24;
        if (hoursInTravel % 24 > 0) {
            daysInTravel++;
        }
        return daysInTravel;
    }
}
