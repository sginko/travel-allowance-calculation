package pl.sginko.travelexpense.logic.overnightStay.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.logic.overnightStay.model.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

@Component
public class OvernightStayMapper {

    public OvernightStayEntity toEntity(OvernightStayDto requestDto, TravelEntity travelEntity) {
        return new OvernightStayEntity(travelEntity, requestDto.getInputQuantityOfOvernightStayWithoutInvoice(),
                requestDto.getInputQuantityOfOvernightStayWithInvoice(), requestDto.getAmountOfTotalOvernightsStayWithInvoice());
    }

    public OvernightStayResponseDto toResponseDto(OvernightStayEntity entity) {
        return new OvernightStayResponseDto(entity.getId(), entity.getInputQuantityOfOvernightStayWithoutInvoice(),
                entity.getAmountOfTotalOvernightsStayWithoutInvoice(), entity.getInputQuantityOfOvernightStayWithInvoice(),
                entity.getAmountOfTotalOvernightsStayWithInvoice(), entity.getOvernightStayAmount());
    }
}
