package pl.sginko.travelexpense.logic.transport.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.transport.exception.TransportException;
import pl.sginko.travelexpense.logic.travelexpense.transport.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.transport.service.TransportCostService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class TransportCostServiceTest {
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);
    private final BigDecimal COST_BY_CAR_ENGINE_UP_TO_900_CC = BigDecimal.valueOf(0.89);
    private final BigDecimal COST_BY_CAR_ENGINE_ABOVE_TO_900_CC = BigDecimal.valueOf(1.15);
    private final BigDecimal COST_BY_MOTORCYCLE = BigDecimal.valueOf(0.69);
    private final BigDecimal COST_BY_MOPED = BigDecimal.valueOf(0.42);

    @Autowired
    private TransportCostService transportCostService;

    @MockBean
    private TravelRequestDto travelRequestDto;

    @MockBean
    private DietDto dietDto;

    @MockBean
    private TransportCostDto transportCostDto;

    @Test
    void should_calculate_cost_of_travel_by_public_transport() {
        //GIVEN
        BigDecimal costOfTravelByPublicTransport = BigDecimal.valueOf(100);

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getCostOfTravelByPublicTransport()).thenReturn(costOfTravelByPublicTransport);

        //WHEN
        BigDecimal calculateCostOfTravelByPublicTransport = transportCostService.calculateCostOfTravelByPublicTransport(travelRequestDto);

        //THEN
        assertThat(calculateCostOfTravelByPublicTransport).isEqualByComparingTo(costOfTravelByPublicTransport);
    }

    @Test
    void should_calculate_undocumented_local_transport_cost() {
        //GIVEN
        BigDecimal dailyUndocumentedLocalTransportCost = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.20));

        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getInputtedDaysNumberForUndocumentedLocalTransportCost()).thenReturn(1);

        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(9, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(20, 59));

        //WHEN
        BigDecimal calculateUndocumentedLocalTransportCost = transportCostService.calculateUndocumentedLocalTransportCost(travelRequestDto);

        //THEN
        assertThat(calculateUndocumentedLocalTransportCost).isEqualByComparingTo(dailyUndocumentedLocalTransportCost);
    }

    @Test
    void should_throw_transportException_for_trip_one_day_and_inputted_two_day_undocumented_cost() {
        //GIVEN
        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getInputtedDaysNumberForUndocumentedLocalTransportCost()).thenReturn(2);

        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(9, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(20, 59));

        //WHEN
        Executable e = () -> transportCostService.calculateUndocumentedLocalTransportCost(travelRequestDto);

        //THEN
        assertThrows(TransportException.class, e);
    }

    @Test
    void should_calculate_documented_local_transport_cost() {
        //GIVEN
        BigDecimal documentedLocalTransportCost = BigDecimal.valueOf(55.00);

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getDocumentedLocalTransportCost()).thenReturn(documentedLocalTransportCost);

        //WHEN
        BigDecimal calculateDocumentedLocalTransportCost = transportCostService.calculateDocumentedLocalTransportCost(travelRequestDto);

        //THEN
        assertThat(calculateDocumentedLocalTransportCost).isEqualByComparingTo(documentedLocalTransportCost);
    }

    @Test
    void should_calculate_cost_of_travel_by_car_engine_up_to_900_cc() {
        //GIVEN
        long kilometres = 100L;

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getKilometersByCarEngineUpTo900cc()).thenReturn(kilometres);

        //WHEN
        BigDecimal calculateCostOfTravelByOwnTransport = transportCostService.calculateCostOfTravelByOwnTransport(travelRequestDto);

        //THEN
        assertThat(calculateCostOfTravelByOwnTransport).isEqualByComparingTo(COST_BY_CAR_ENGINE_UP_TO_900_CC.multiply(BigDecimal.valueOf(kilometres)));
    }

    @Test
    void should_calculate_cost_of_travel_by_car_engine_above_to_900_cc() {
        //GIVEN
        long kilometres = 100L;

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getKilometersByCarEngineAbove900cc()).thenReturn(kilometres);

        //WHEN
        BigDecimal calculateCostOfTravelByOwnTransport = transportCostService.calculateCostOfTravelByOwnTransport(travelRequestDto);

        //THEN
        assertThat(calculateCostOfTravelByOwnTransport).isEqualByComparingTo(COST_BY_CAR_ENGINE_ABOVE_TO_900_CC.multiply(BigDecimal.valueOf(kilometres)));
    }

    @Test
    void should_calculate_cost_of_travel_by_motorcycle() {
        //GIVEN
        long kilometres = 100L;

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getKilometersByMotorcycle()).thenReturn(kilometres);

        //WHEN
        BigDecimal calculateCostOfTravelByOwnTransport = transportCostService.calculateCostOfTravelByOwnTransport(travelRequestDto);

        //THEN
        assertThat(calculateCostOfTravelByOwnTransport).isEqualByComparingTo(COST_BY_MOTORCYCLE.multiply(BigDecimal.valueOf(kilometres)));
    }

    @Test
    void should_calculate_cost_of_travel_by_moped() {
        //GIVEN
        long kilometres = 100L;

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getKilometersByMoped()).thenReturn(kilometres);

        //WHEN
        BigDecimal calculateCostOfTravelByOwnTransport = transportCostService.calculateCostOfTravelByOwnTransport(travelRequestDto);

        //THEN
        assertThat(calculateCostOfTravelByOwnTransport).isEqualByComparingTo(COST_BY_MOPED.multiply(BigDecimal.valueOf(kilometres)));
    }

    @Test
    void should_calculate_total_cost_of_travel_by_own_and_public_transport() {
        //GIVEN
        long kilometres = 100L;
        BigDecimal costOfTravelByPublicTransport = BigDecimal.valueOf(100);

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getCostOfTravelByPublicTransport()).thenReturn(costOfTravelByPublicTransport);
        when(transportCostDto.getKilometersByMoped()).thenReturn(kilometres);

        //WHEN
        BigDecimal calculateTotalCostOfTravelByOwnAndPublicTransport = transportCostService.calculateTotalCostOfTravelByOwnAndPublicTransport(travelRequestDto);

        //THEN
        assertThat(calculateTotalCostOfTravelByOwnAndPublicTransport).isEqualByComparingTo((COST_BY_MOPED.multiply(BigDecimal.valueOf(kilometres))
                .add(costOfTravelByPublicTransport)));
    }

    @Test
    void should_calculate_transport_cost_amount() {
        //GIVEN
        BigDecimal dailyUndocumentedLocalTransportCost = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.20));
        BigDecimal documentedLocalTransportCost = BigDecimal.valueOf(55.00);
        BigDecimal costOfTravelByPublicTransport = BigDecimal.valueOf(100);
        long kilometres = 100L;

        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);

        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);
        when(transportCostDto.getDocumentedLocalTransportCost()).thenReturn(documentedLocalTransportCost);
        when(transportCostDto.getInputtedDaysNumberForUndocumentedLocalTransportCost()).thenReturn(1);
        when(transportCostDto.getCostOfTravelByPublicTransport()).thenReturn(costOfTravelByPublicTransport);
        when(transportCostDto.getKilometersByMoped()).thenReturn(kilometres);

        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(9, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(20, 59));

        //WHEN
        BigDecimal transportCostAmount = transportCostService.calculateTransportCostAmount(travelRequestDto);

        //THEN
        assertThat(transportCostAmount).isEqualByComparingTo((COST_BY_MOPED.multiply(BigDecimal.valueOf(kilometres))
                .add(dailyUndocumentedLocalTransportCost).add(documentedLocalTransportCost).add(costOfTravelByPublicTransport)));
    }
}
