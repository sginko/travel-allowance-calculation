package pl.sginko.travelexpense.logic.travelexpense.diet.service;

import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface DietService {

    BigDecimal calculateDiet(final TravelRequestDto travelRequestDto);

    BigDecimal calculateDietAmount(final TravelRequestDto travelRequestDto);

    BigDecimal calculateFoodAmount(final TravelRequestDto travelRequestDto);
}
