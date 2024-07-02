package pl.sginko.travelexpense.model.diet.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.diet.dto.DietRequestDto;
import pl.sginko.travelexpense.model.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.model.diet.entity.DietEntity;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

@Component
public class DietMapper {
    public DietEntity toEntity(DietRequestDto requestDto, TravelEntity travelEntity) {
        return new DietEntity(travelEntity, requestDto.getNumberOfBreakfasts(), requestDto.getNumberOfLunches(),
                requestDto.getNumberOfDinners());
    }

    public DietResponseDto toResponseDto(DietEntity entity) {
        return new DietResponseDto(entity.getId(), entity.getDietAmount(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners());
    }
}
