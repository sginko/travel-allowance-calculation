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
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietDto;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class DietMapperTest {
    private DietMapper dietMapper;
    private TravelReportEntity travelReportEntity;

    @BeforeEach
    void setUp() {
        dietMapper = new DietMapper();
        travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Test
    void should_map_DietDto_to_DietEntity_correctly() {
        // GIVEN
        DietDto dietDto = new DietDto(BigDecimal.valueOf(45), 1, 1, 1);

        // WHEN
        DietEntity dietEntity = dietMapper.toEntity(dietDto, travelReportEntity);

        // THEN
        assertThat(dietEntity).isNotNull();
        assertThat(dietEntity.getDailyAllowance()).isEqualByComparingTo(BigDecimal.valueOf(45));
        assertThat(dietEntity.getNumberOfBreakfasts()).isEqualTo(1);
        assertThat(dietEntity.getNumberOfLunches()).isEqualTo(1);
        assertThat(dietEntity.getNumberOfDinners()).isEqualTo(1);
        assertThat(dietEntity.getTravelReportEntity()).isEqualTo(travelReportEntity);
    }

    @Test
    void should_map_DietEntity_to_DietResponseDto_correctly() {
        // GIVEN
        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45), 1, 1, 1);

        // WHEN
        DietResponseDto dietResponseDto = dietMapper.toResponseDto(dietEntity);

        // THEN
        assertThat(dietResponseDto).isNotNull();
        assertThat(dietResponseDto.getId()).isEqualTo(dietEntity.getId());
        assertThat(dietResponseDto.getNumberOfBreakfasts()).isEqualTo(dietEntity.getNumberOfBreakfasts());
        assertThat(dietResponseDto.getNumberOfLunches()).isEqualTo(dietEntity.getNumberOfLunches());
        assertThat(dietResponseDto.getNumberOfDinners()).isEqualTo(dietEntity.getNumberOfDinners());
        assertThat(dietResponseDto.getFoodAmount()).isEqualByComparingTo(dietEntity.getFoodAmount());
        assertThat(dietResponseDto.getDietAmount()).isEqualByComparingTo(dietEntity.getDietAmount());
    }

    @Test
    void should_map_DietEntity_to_DietEditDto_correctly() {
        // GIVEN
        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45), 2,
                3, 1);

        // WHEN
        DietEditDto dietEditDto = dietMapper.toDietEditDto(dietEntity);

        // THEN
        assertThat(dietEditDto).isNotNull();
        assertThat(dietEditDto.getDailyAllowance()).isEqualByComparingTo(dietEntity.getDailyAllowance());
        assertThat(dietEditDto.getNumberOfBreakfasts()).isEqualTo(dietEntity.getNumberOfBreakfasts());
        assertThat(dietEditDto.getNumberOfLunches()).isEqualTo(dietEntity.getNumberOfLunches());
        assertThat(dietEditDto.getNumberOfDinners()).isEqualTo(dietEntity.getNumberOfDinners());
    }
}
