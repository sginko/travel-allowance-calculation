package pl.sginko.travelexpense.model.diet.service;

import pl.sginko.travelexpense.model.diet.dto.DietRequestDto;
import pl.sginko.travelexpense.model.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.model.diet.entity.DietEntity;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;

public interface DietService {

    DietResponseDto calculateDiet(TravelRequestDto travelRequestDto, DietRequestDto dietRequestDto, DietEntity dietEntity);
}
