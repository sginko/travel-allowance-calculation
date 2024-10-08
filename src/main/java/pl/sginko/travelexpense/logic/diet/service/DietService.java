package pl.sginko.travelexpense.logic.diet.service;

import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface DietService {

    BigDecimal calculateDiet(final TravelRequestDto travelRequestDto);

    BigDecimal calculateDietAmount(final TravelRequestDto travelRequestDto);

    BigDecimal calculateFoodAmount(final TravelRequestDto travelRequestDto);
}
