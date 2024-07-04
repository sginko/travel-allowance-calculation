package pl.sginko.travelexpense.logic.diet.service;

import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.diet.entity.DietEntity;

import java.math.BigDecimal;

public interface DietService {

    BigDecimal calculateDiet(TravelRequestDto travelRequestDto);
}
