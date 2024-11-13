package pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayEditDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

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

    public OvernightStayEntity toEntity(OvernightStayDto overnightStayDto, TravelEntity travelEntity) {
        return new OvernightStayEntity(
                travelEntity,
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
