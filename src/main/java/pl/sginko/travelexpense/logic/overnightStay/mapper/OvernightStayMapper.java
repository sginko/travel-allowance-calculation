package pl.sginko.travelexpense.logic.overnightStay.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.logic.overnightStay.model.entity.OvernightStayEntity;

@Component
public class OvernightStayMapper {

    public OvernightStayResponseDto toResponseDto(OvernightStayEntity entity) {
        return new OvernightStayResponseDto(entity.getId(), entity.getQuantityOfOvernightStay(),
                entity.getTotalInputQuantityOfOvernightStay(), entity.getInputQuantityOfOvernightStayWithoutInvoice(),
                entity.getAmountOfTotalOvernightsStayWithoutInvoice(), entity.getInputQuantityOfOvernightStayWithInvoice(),
                entity.getAmountOfTotalOvernightsStayWithInvoice(), entity.getOvernightStayAmount());
    }
}
