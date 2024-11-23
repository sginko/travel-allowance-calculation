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
package pl.sginko.travelexpense.domain.travelReport.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TransportCostEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.mapper.TransportCostMapper;
import pl.sginko.travelexpense.domain.travelReport.service.transportCost.TransportCostServiceImpl;

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
