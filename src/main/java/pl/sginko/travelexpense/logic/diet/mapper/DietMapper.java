package pl.sginko.travelexpense.logic.diet.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.diet.model.dto.DietDto;
import pl.sginko.travelexpense.logic.diet.model.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.diet.model.entity.DietEntity;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

@Component
public class DietMapper {

    public DietEntity toEntity(DietDto dietDto, TravelRequestDto travelRequestDto) {
        return new DietEntity(dietDto.getDailyAllowance(), dietDto.getNumberOfBreakfasts(),
                dietDto.getNumberOfLunches(), dietDto.getNumberOfDinners());
    }

    public DietResponseDto toResponseDto(DietEntity entity) {
        return new DietResponseDto(entity.getId(), entity.getDietAmount(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners());
    }
}
