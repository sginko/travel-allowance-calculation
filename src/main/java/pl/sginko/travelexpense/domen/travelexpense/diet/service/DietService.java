package pl.sginko.travelexpense.domen.travelexpense.diet.service;

import pl.sginko.travelexpense.domen.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.domen.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelEntity;

public interface DietService {

    DietEntity createDietEntity(DietDto dietDto, TravelEntity travelEntity);
}
