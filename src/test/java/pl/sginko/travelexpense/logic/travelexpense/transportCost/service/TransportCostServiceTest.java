package pl.sginko.travelexpense.logic.travelexpense.transportCost.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportCostServiceTest {
    @Mock
    private TransportCostMapper transportCostMapper;

    @Mock
    private TransportCostDto transportCostDto;

    @InjectMocks
    private TransportCostServiceImpl transportCostService;

    @Test
    void should_create_transport_cost_entity() {
        //GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB", LocalDate.now(),
                LocalTime.of(8, 0), LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelReportEntity.setDietDetails(dietEntity);

        TransportCostEntity transportCostEntity = new TransportCostEntity(travelReportEntity,
                2, BigDecimal.valueOf(100), "Bus",
                BigDecimal.valueOf(50), 100L, 200L,
                50L, 30L);

        when(transportCostMapper.toEntity(transportCostDto, travelReportEntity)).thenReturn(transportCostEntity);

        // WHEN
        TransportCostEntity result = transportCostService.createTransportCostEntity(transportCostDto, travelReportEntity);

        // THEN
        assertThat(result).isEqualTo(transportCostEntity);
        verify(transportCostMapper, times(1)).toEntity(transportCostDto, travelReportEntity);
    }
}
