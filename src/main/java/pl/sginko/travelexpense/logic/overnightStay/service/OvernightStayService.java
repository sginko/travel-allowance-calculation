package pl.sginko.travelexpense.logic.overnightStay.service;

import pl.sginko.travelexpense.logic.overnightStay.model.dto.CalculatedOvernightStay;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

public interface OvernightStayService {

    CalculatedOvernightStay calculateOvernightStay(TravelRequestDto travelRequestDto);
}
