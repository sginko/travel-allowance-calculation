package pl.sginko.travelexpense.model.overnightStayExpenses.service;

import pl.sginko.travelexpense.model.dietExpenses.dto.DietExpensesRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface OvernightStayService {

    BigDecimal calculateOvernightStayAmount(DietExpensesRequestDto dietRequestDto, TravelRequestDto travelRequestDto);
}
