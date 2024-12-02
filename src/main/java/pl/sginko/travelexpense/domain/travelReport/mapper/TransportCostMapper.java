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
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TransportCostEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

@Component
public class TransportCostMapper {

    public TransportCostResponseDto toResponseDto(TransportCostEntity entity) {
        return new TransportCostResponseDto(
                entity.getId(),
                entity.getDaysForUndocumentedLocalTransportCost(),
                entity.getUndocumentedLocalTransportCost(),
                entity.getDocumentedLocalTransportCost(),
                entity.getMeansOfTransport(),
                entity.getCostOfTravelByPublicTransport(),
                entity.getCostOfTravelByOwnTransport(),
                entity.getTransportCostAmount());
    }

    public TransportCostEntity toEntity(TransportCostDto transportCostDto, TravelReportEntity travelReportEntity) {
        return new TransportCostEntity(
                travelReportEntity,
                transportCostDto.getDaysForUndocumentedLocalTransportCost(),
                transportCostDto.getDocumentedLocalTransportCost(),
                transportCostDto.getMeansOfTransport(),
                transportCostDto.getCostOfTravelByPublicTransport(),
                transportCostDto.getKilometersByCarEngineUpTo900cc(),
                transportCostDto.getKilometersByCarEngineAbove900cc(),
                transportCostDto.getKilometersByMotorcycle(),
                transportCostDto.getKilometersByMoped());
    }

    public TransportCostEditDto toTransportCosEditDto(TransportCostEntity entity) {
        return new TransportCostEditDto(
                entity.getDaysForUndocumentedLocalTransportCost(),
                entity.getDocumentedLocalTransportCost(),
                entity.getMeansOfTransport(),
                entity.getCostOfTravelByPublicTransport(),
                entity.getKilometersByCarEngineUpTo900cc(),
                entity.getKilometersByCarEngineAbove900cc(),
                entity.getKilometersByMotorcycle(),
                entity.getKilometersByMoped());
    }
}
