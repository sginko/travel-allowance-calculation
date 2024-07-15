package pl.sginko.travelexpense.logic.diet.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.diet.model.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.diet.model.entity.DietEntity;

@Component
public class DietMapper {

    public DietResponseDto toResponseDto(DietEntity entity) {
        return new DietResponseDto(entity.getId(), entity.getNumberOfBreakfasts(), entity.getNumberOfLunches(),
                entity.getNumberOfDinners(), entity.getFoodAmount(), entity.getDietAmount());
    }
}
