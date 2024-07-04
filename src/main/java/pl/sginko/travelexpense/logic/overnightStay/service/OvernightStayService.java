package pl.sginko.travelexpense.logic.overnightStay.service;

import pl.sginko.travelexpense.logic.overnightStay.dto.CalculatedOvernightStay;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.logic.overnightStay.entity.OvernightStayEntity;

public interface OvernightStayService {

    CalculatedOvernightStay calculateOvernightStay(TravelRequestDto travelRequestDto);
}
