package pl.sginko.travelexpense.model.Mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.Dto.TravelRequestDto;
import pl.sginko.travelexpense.model.Dto.TravelResponseDto;
import pl.sginko.travelexpense.model.Entity.TravelEntity;

@Component
public class TravelMapper {

    public TravelResponseDto fromEntity(TravelEntity travelEntity) {
        return new TravelResponseDto(travelEntity.getId(), travelEntity.getStartTravel(), travelEntity.getFinishTravel(),
                travelEntity.getBreakfastQuantity(), travelEntity.getLunchQuantity(), travelEntity.getDinnerQuantity(),
                travelEntity.getTotalAmount(), travelEntity.getDietAmount(), travelEntity.getFoodAmount());
    }

    public TravelEntity toEntityAfterCalculation(TravelRequestDto travelRequestDto) {
        return new TravelEntity(travelRequestDto.getStartDateTime(), travelRequestDto.getEndDateTime(),
                travelRequestDto.getDAILY_ALLOWANCE(), travelRequestDto.getBreakfastQuantity(),
                travelRequestDto.getLunchQuantity(), travelRequestDto.getDinnerQuantity());
    }
}