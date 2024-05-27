package pl.sginko.travelallowance.model.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;
import pl.sginko.travelallowance.model.Entity.TravelEntity;

import java.math.BigDecimal;

@Component
public class TravelMapper {
    private final ModelMapper modelMapper;

    public TravelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

//    public TravelResponseDto fromEntity(TravelEntity travelEntity) {
//        TravelResponseDto map = modelMapper.map(travelEntity, TravelResponseDto.class);
//        return map;
//    }

    public TravelResponseDto fromEntity(TravelEntity travelEntity) {
        TravelResponseDto map = new TravelResponseDto(travelEntity.getStartDateTime(), travelEntity.getEndDateTime(),
                travelEntity.getBreakfastQuantity(), travelEntity.getLunchQuantity(), travelEntity.getDinnerQuantity(),
                travelEntity.getCostOfTotalExpense());
        return map;
    }

    public TravelEntity toEntity(TravelRequestDto travelRequestDto) {
        return modelMapper.map(travelRequestDto, TravelEntity.class);
    }

    public TravelEntity toEntityAfterCalculation(TravelRequestDto travelRequestDto, BigDecimal costOfTotalExpense) {
        return new TravelEntity(travelRequestDto.getStartDateTime(), travelRequestDto.getEndDateTime(),
                travelRequestDto.getDAILY_ALLOWANCE(), travelRequestDto.getBreakfastQuantity(),
                travelRequestDto.getLunchQuantity(), travelRequestDto.getDinnerQuantity(), costOfTotalExpense);
    }
}