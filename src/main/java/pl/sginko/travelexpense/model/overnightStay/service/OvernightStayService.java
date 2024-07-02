package pl.sginko.travelexpense.model.overnightStay.service;

import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayRequestDto;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

public interface OvernightStayService {

    OvernightStayResponseDto calculateOvernightStay(OvernightStayRequestDto requestDto, TravelEntity travelEntity);

//    BigDecimal amountOfTotalOvernightsStayWithInvoice(TravelRequestDto travelRequestDto);
}
