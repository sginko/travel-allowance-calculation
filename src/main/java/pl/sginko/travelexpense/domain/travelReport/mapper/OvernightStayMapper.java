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