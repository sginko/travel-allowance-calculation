package pl.sginko.travelexpense.logic.diet.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.diet.model.dto.DietDto;
import pl.sginko.travelexpense.logic.diet.model.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.diet.model.entity.DietEntity;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

@Component
public class DietMapper {

    public DietEntity toEntity(DietDto requestDto, TravelEntity travelEntity) {
        return new DietEntity(travelEntity, requestDto.getDailyAllowance(), requestDto.getNumberOfBreakfasts(),
                requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners());
    }

//    public DietEntity toEntity(DietDto dietDto, TravelRequestDto travelRequestDto) {
//        TravelEntity travelEntity = travelMapper.toEntity(travelRequestDto);
//        return new DietEntity(travelEntity, dietDto.getDailyAllowance(), dietDto.getNumberOfBreakfasts(),
//                dietDto.getNumberOfLunches(), dietDto.getNumberOfDinners());
//    }

    public DietResponseDto toResponseDto(DietEntity entity) {
        return new DietResponseDto(entity.getId(), entity.getDietAmount(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners());
    }
}
