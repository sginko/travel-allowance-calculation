package pl.sginko.travelexpense.logic.travelexpense.diet.service;

import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;

public interface DietService {

    DietEntity createDietEntity(DietDto dietDto, TravelEntity travelEntity);
}
