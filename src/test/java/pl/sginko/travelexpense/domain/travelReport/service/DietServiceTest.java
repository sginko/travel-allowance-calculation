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
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.mapper.DietMapper;
import pl.sginko.travelexpense.domain.travelReport.service.diet.DietServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DietServiceTest {
    @Mock
    private DietMapper dietMapper;

    @InjectMocks
    private DietServiceImpl dietService;

    @Mock
    private DietDto dietDto;

    @Test
    void should_create_DietEntity() {
        //GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB", LocalDate.now(),
                LocalTime.of(8, 0), LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45),
                1, 1, 1);

        when(dietMapper.toEntity(dietDto, travelReportEntity)).thenReturn(dietEntity);

        // WHEN
        DietEntity result = dietService.createDietEntity(dietDto, travelReportEntity);

        // THEN
        assertThat(result).isEqualTo(dietEntity);
        verify(dietMapper).toEntity(dietDto, travelReportEntity);
    }
}
