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
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.domain.travelReport.service.overnightStay.OvernightStayServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OvernightStayServiceTest {
    @Mock
    private OvernightStayMapper overnightStayMapper;

    @Mock
    private OvernightStayDto overnightStayDto;

    @InjectMocks
    private OvernightStayServiceImpl overnightStayService;

    @Test
    void should_create_overnightStay_entity() {
        //GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(20, 0),
                LocalDate.now().plusDays(1), LocalTime.of(6, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelReportEntity.setDietDetails(dietEntity);

        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                1, 0,
                BigDecimal.valueOf(500), false);

        when(overnightStayMapper.toEntity(overnightStayDto, travelReportEntity)).thenReturn(overnightStayEntity);

        // WHEN
        OvernightStayEntity result = overnightStayService.createOvernightStayEntity(overnightStayDto, travelReportEntity);

        // THEN
        assertThat(result).isEqualTo(overnightStayEntity);
        verify(overnightStayMapper, times(1)).toEntity(overnightStayDto, travelReportEntity);
    }
}
