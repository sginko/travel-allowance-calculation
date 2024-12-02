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
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayDto;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

@Component
public class OvernightStayMapper {

    public OvernightStayResponseDto toResponseDto(OvernightStayEntity entity) {
        return new OvernightStayResponseDto(
                entity.getId(),
                entity.getQuantityOfOvernightStay(),
                entity.getTotalInputQuantityOfOvernightStay(),
                entity.getInputQuantityOfOvernightStayWithoutInvoice(),
                entity.getTotalAmountOfOvernightsStayWithoutInvoice(),
                entity.getInputQuantityOfOvernightStayWithInvoice(),
                entity.getTotalAmountOfOvernightsStayWithInvoice(),
                entity.getOvernightStayAmount());
    }

    public OvernightStayEntity toEntity(OvernightStayDto overnightStayDto, TravelReportEntity travelReportEntity) {
        return new OvernightStayEntity(
                travelReportEntity,
                overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice(),
                overnightStayDto.getInputQuantityOfOvernightStayWithInvoice(),
                overnightStayDto.getTotalAmountOfOvernightsStayWithInvoice(),
                overnightStayDto.getIsInvoiceAmountGreaterAllowed());
    }

    public OvernightStayEditDto toOvernightStayEditDto(OvernightStayEntity entity) {
        return new OvernightStayEditDto(
                entity.getInputQuantityOfOvernightStayWithoutInvoice(),
                entity.getInputQuantityOfOvernightStayWithInvoice(),
                entity.getTotalAmountOfOvernightsStayWithInvoice(),
                entity.getIsInvoiceAmountGreaterAllowed());
    }
}
