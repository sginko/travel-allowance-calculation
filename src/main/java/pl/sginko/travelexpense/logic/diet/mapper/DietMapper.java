package pl.sginko.travelexpense.logic.diet.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;
import pl.sginko.travelexpense.logic.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.diet.entity.DietEntity;

@Component
public class DietMapper {

    public DietEntity toEntity(DietDto requestDto, TravelEntity travelEntity) {
        return new DietEntity(travelEntity, requestDto.getDailyAllowance(), requestDto.getNumberOfBreakfasts(),
                requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners());
    }

    public DietResponseDto toResponseDto(DietEntity entity) {
        return new DietResponseDto(entity.getId(), entity.getDietAmount(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners());
    }
}