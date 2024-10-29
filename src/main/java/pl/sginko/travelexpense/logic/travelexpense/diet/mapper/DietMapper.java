package pl.sginko.travelexpense.logic.travelexpense.diet.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

@Component
public class DietMapper {

    public DietResponseDto toResponseDto(DietEntity entity) {
        return new DietResponseDto(
                entity.getId(),
                entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(),
                entity.getNumberOfDinners(),
                entity.getFoodAmount(),
                entity.getDietAmount());
    }

    public DietEntity toEntity(DietDto dietDto, TravelEntity travelEntity) {
        return new DietEntity(
                travelEntity,
                dietDto.getDailyAllowance(),
                dietDto.getNumberOfBreakfasts(),
                dietDto.getNumberOfLunches(),
                dietDto.getNumberOfDinners()
        );
    }
}
