package pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransportCostMapperTest {
    private TransportCostMapper transportCostMapper;
    private TravelEntity travelEntity;

    @BeforeEach
    void setUp() {
        transportCostMapper = new TransportCostMapper();
        travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(2), LocalTime.of(20, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);
        travelEntity.setDietDetails(dietEntity);
    }

    @Test
    void should_map_TransportCostDto_to_TransportCostEntity_correctly() {
        // GIVEN
        TransportCostDto transportCostDto = new TransportCostDto(2,
                BigDecimal.valueOf(300), "Bus", BigDecimal.valueOf(150), 100L,
                200L, 50L, 30L);

        // WHEN
        TransportCostEntity transportCostEntity = transportCostMapper.toEntity(transportCostDto, travelEntity);

        // THEN
        assertThat(transportCostEntity).isNotNull();
        assertThat(transportCostEntity.getDaysForUndocumentedLocalTransportCost()).isEqualTo(2);
        assertThat(transportCostEntity.getDocumentedLocalTransportCost()).isEqualByComparingTo(BigDecimal.valueOf(300));
        assertThat(transportCostEntity.getMeansOfTransport()).isEqualTo("Bus");
        assertThat(transportCostEntity.getCostOfTravelByPublicTransport()).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(transportCostEntity.getKilometersByCarEngineUpTo900cc()).isEqualTo(100L);
        assertThat(transportCostEntity.getKilometersByCarEngineAbove900cc()).isEqualTo(200L);
        assertThat(transportCostEntity.getKilometersByMotorcycle()).isEqualTo(50L);
        assertThat(transportCostEntity.getKilometersByMoped()).isEqualTo(30L);
        assertThat(transportCostEntity.getTravelEntity()).isEqualTo(travelEntity);
    }

    @Test
    void should_map_TransportCostEntity_to_TransportCostResponseDto_correctly() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelEntity, 2,
                BigDecimal.valueOf(300), "Bus", BigDecimal.valueOf(150), 100L,
                200L, 50L, 30L);

        // WHEN
        TransportCostResponseDto transportCostResponseDto = transportCostMapper.toResponseDto(transportCostEntity);

        // THEN
        assertThat(transportCostResponseDto).isNotNull();
        assertThat(transportCostResponseDto.getId()).isEqualTo(transportCostEntity.getId());
        assertThat(transportCostResponseDto.getDaysForTransportCost()).isEqualTo(transportCostEntity.getDaysForUndocumentedLocalTransportCost());
        assertThat(transportCostResponseDto.getUndocumentedLocalTransportCost())
                .isEqualByComparingTo(transportCostEntity.getUndocumentedLocalTransportCost());
        assertThat(transportCostResponseDto.getDocumentedLocalTransportCost())
                .isEqualByComparingTo(transportCostEntity.getDocumentedLocalTransportCost());
        assertThat(transportCostResponseDto.getMeansOfTransport()).isEqualTo(transportCostEntity.getMeansOfTransport());
        assertThat(transportCostResponseDto.getCostOfTravelByPublicTransport())
                .isEqualByComparingTo(transportCostEntity.getCostOfTravelByPublicTransport());
        assertThat(transportCostResponseDto.getCostOfTravelByOwnTransport())
                .isEqualByComparingTo(transportCostEntity.getCostOfTravelByOwnTransport());
        assertThat(transportCostResponseDto.getTransportCostAmount())
                .isEqualByComparingTo(transportCostEntity.getTransportCostAmount());
    }
}
