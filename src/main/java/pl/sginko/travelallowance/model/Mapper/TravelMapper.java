package pl.sginko.travelallowance.model.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;
import pl.sginko.travelallowance.model.Entity.TravelEntity;

import java.math.BigDecimal;

@Component
public class TravelMapper {

    public TravelResponseDto fromEntity(TravelEntity travelEntity) {
        return new TravelResponseDto(travelEntity.getStartTravel(), travelEntity.getFinishTravel(),
                travelEntity.getBreakfastQuantity(), travelEntity.getLunchQuantity(), travelEntity.getDinnerQuantity(),
                travelEntity.getTotalAmount(), travelEntity.getDietAmount(), travelEntity.getFoodAmount());
    }

    public TravelEntity toEntityAfterCalculation(TravelRequestDto travelRequestDto) {
        return new TravelEntity(travelRequestDto.getStartDateTime(), travelRequestDto.getEndDateTime(),
                travelRequestDto.getDAILY_ALLOWANCE(), travelRequestDto.getBreakfastQuantity(),
                travelRequestDto.getLunchQuantity(), travelRequestDto.getDinnerQuantity());
    }
}