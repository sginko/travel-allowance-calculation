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

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietDto;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

@Component
public class DietMapper {

    public DietResponseDto toResponseDto(DietEntity entity) {
        return new DietResponseDto(
                entity.getId(),
                entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(),
                entity.getNumberOfDinners(),
                entity.getFoodAmount(),
                entity.getDietAmount());
    }

    public DietEntity toEntity(DietDto dietDto, TravelReportEntity travelReportEntity) {
        return new DietEntity(
                travelReportEntity,
                dietDto.getDailyAllowance(),
                dietDto.getNumberOfBreakfasts(),
                dietDto.getNumberOfLunches(),
                dietDto.getNumberOfDinners());
    }

    public DietEditDto toDietEditDto(DietEntity entity) {
        return new DietEditDto(
                entity.getDailyAllowance(),
                entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(),
                entity.getNumberOfDinners());
    }
}
