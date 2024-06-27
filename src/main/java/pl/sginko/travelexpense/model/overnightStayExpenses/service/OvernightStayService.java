package pl.sginko.travelexpense.model.overnightStayExpenses.service;

import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface OvernightStayService {

    BigDecimal calculateOvernightStayAmount(TravelRequestDto travelRequestDto);
}
