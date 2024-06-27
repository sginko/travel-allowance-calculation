package pl.sginko.travelexpense.model.dietExpenses.service;

import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface DietExpensesService {

    BigDecimal calculateDietAmount(TravelRequestDto requestDto);

    BigDecimal calculateFoodAmount(TravelRequestDto requestDto);

    BigDecimal getDAILY_ALLOWANCE();
}
