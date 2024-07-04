package pl.sginko.travelexpense.model.overnightStay.mapper;

import jdk.jfr.Category;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayRequestDto;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.model.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

@Component
public class OvernightStayMapper {
    public OvernightStayEntity toEntity(OvernightStayRequestDto requestDto, TravelEntity travelEntity) {
        return new OvernightStayEntity(travelEntity, requestDto.getInputQuantityOfOvernightStayWithoutInvoice(),
                requestDto.getInputQuantityOfOvernightStayWithInvoice(), requestDto.getAmountOfTotalOvernightsStayWithInvoice());
    }

    public OvernightStayResponseDto toResponseDto(OvernightStayEntity entity) {
        return new OvernightStayResponseDto(entity.getId(), entity.getInputQuantityOfOvernightStayWithoutInvoice(),
                entity.getAmountOfTotalOvernightsStayWithoutInvoice(), entity.getInputQuantityOfOvernightStayWithInvoice(),
                entity.getAmountOfTotalOvernightsStayWithInvoice(), entity.getOvernightStayAmount());
    }
}