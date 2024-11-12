package pl.sginko.travelexpense.logic.travelexpense.diet.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

@AllArgsConstructor
@Service
class DietServiceImpl implements DietService {
    private final DietMapper dietMapper;

    @Override
    public DietEntity createDietEntity(DietDto dietDto, TravelEntity travelEntity) {
        DietEntity dietEntity = dietMapper.toEntity(dietDto, travelEntity);
        return dietEntity;
    }
}
