/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.domain.travelReport.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TransportCostEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransportCostMapperTest {
    private TransportCostMapper transportCostMapper;
    private TravelReportEntity travelReportEntity;

    @BeforeEach
    void setUp() {
        transportCostMapper = new TransportCostMapper();
        travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(2), LocalTime.of(20, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45), 0, 0, 0);
        travelReportEntity.setDietDetails(dietEntity);
    }

    @Test
    void should_map_TransportCostDto_to_TransportCostEntity_correctly() {
        // GIVEN
        TransportCostDto transportCostDto = new TransportCostDto(2,
                BigDecimal.valueOf(300), "Bus", BigDecimal.valueOf(150), 100L,
                200L, 50L, 30L);

        // WHEN
        TransportCostEntity transportCostEntity = transportCostMapper.toEntity(transportCostDto, travelReportEntity);

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
        assertThat(transportCostEntity.getTravelReportEntity()).isEqualTo(travelReportEntity);
    }

    @Test
    void should_map_TransportCostEntity_to_TransportCostResponseDto_correctly() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelReportEntity, 2,
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

    @Test
    void should_map_TransportCostEntity_to_TransportCostEditDto_correctly() {
        // GIVEN
        TransportCostEntity transportCostEntity = new TransportCostEntity(travelReportEntity, 2,
                BigDecimal.valueOf(300), "Bus", BigDecimal.valueOf(150), 100L,
                200L, 50L, 30L);

        // WHEN
        TransportCostEditDto transportCostEditDto = transportCostMapper.toTransportCosEditDto(transportCostEntity);

        // THEN
        assertThat(transportCostEditDto).isNotNull();
        assertThat(transportCostEditDto.getDaysForUndocumentedLocalTransportCost())
                .isEqualTo(transportCostEntity.getDaysForUndocumentedLocalTransportCost());
        assertThat(transportCostEditDto.getDocumentedLocalTransportCost())
                .isEqualByComparingTo(transportCostEntity.getDocumentedLocalTransportCost());
        assertThat(transportCostEditDto.getMeansOfTransport())
                .isEqualTo(transportCostEntity.getMeansOfTransport());
        assertThat(transportCostEditDto.getCostOfTravelByPublicTransport())
                .isEqualByComparingTo(transportCostEntity.getCostOfTravelByPublicTransport());
        assertThat(transportCostEditDto.getKilometersByCarEngineUpTo900cc())
                .isEqualTo(transportCostEntity.getKilometersByCarEngineUpTo900cc());
        assertThat(transportCostEditDto.getKilometersByCarEngineAbove900cc())
                .isEqualTo(transportCostEntity.getKilometersByCarEngineAbove900cc());
        assertThat(transportCostEditDto.getKilometersByMotorcycle())
                .isEqualTo(transportCostEntity.getKilometersByMotorcycle());
        assertThat(transportCostEditDto.getKilometersByMoped())
                .isEqualTo(transportCostEntity.getKilometersByMoped());
    }
}
