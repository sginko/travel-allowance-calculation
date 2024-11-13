package pl.sginko.travelexpense.logic.travelexpense.transportCost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransportCostServiceTest {
    @Mock
    private TransportCostMapper transportCostMapper;

    @Mock
    private TransportCostDto transportCostDto;

    @InjectMocks
    private TransportCostServiceImpl transportCostService;

    private TravelEntity travelEntity;
    private TransportCostEntity transportCostEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelEntity.setDietDetails(dietEntity);

        transportCostEntity = new TransportCostEntity(travelEntity,
                2, BigDecimal.valueOf(100), "Bus",
                BigDecimal.valueOf(50), 100L, 200L,
                50L, 30L);

        when(transportCostMapper.toEntity(transportCostDto, travelEntity)).thenReturn(transportCostEntity);
    }

    @Test
    void should_create_transport_cost_entity() {
        // WHEN
        TransportCostEntity result = transportCostService.createTransportCostEntity(transportCostDto, travelEntity);

        // THEN
        assertThat(result).isEqualTo(transportCostEntity);
        verify(transportCostMapper, times(1)).toEntity(transportCostDto, travelEntity);
    }
}
