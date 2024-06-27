package pl.sginko.travelexpense.model.dietExpenses.service;

import pl.sginko.travelexpense.model.dietExpenses.dto.DietExpensesRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface DietExpensesService {

    BigDecimal calculateDietAmount(DietExpensesRequestDto dietRequestDto, TravelRequestDto travelRequestDto);

    BigDecimal calculateFoodAmount(DietExpensesRequestDto requestDto);
}
