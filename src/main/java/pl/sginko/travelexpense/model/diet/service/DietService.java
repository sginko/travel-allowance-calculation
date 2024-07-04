package pl.sginko.travelexpense.model.diet.service;

import pl.sginko.travelexpense.model.diet.dto.DietRequestDto;
import pl.sginko.travelexpense.model.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

import java.math.BigDecimal;

public interface DietService {

    DietResponseDto calculateDiet(DietRequestDto requestDto, TravelEntity travelEntity);
//    DietResponseDto calculateDiet(TravelRequestDto requestDto, TravelEntity travelEntity);

//    BigDecimal calculateFoodAmount(TravelRequestDto requestDto);
//
//    BigDecimal getDAILY_ALLOWANCE();
}
