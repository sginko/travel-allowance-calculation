package pl.sginko.travelallowance.model.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;
import pl.sginko.travelallowance.model.Entity.TravelEntity;

@Component
public class TravelMapper {
    private final ModelMapper modelMapper;

    public TravelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TravelResponseDto fromEntity(TravelEntity productEntity) {
        return modelMapper.map(productEntity, TravelResponseDto.class);
    }

    public TravelEntity toEntity(TravelRequestDto travelRequestDto) {
        return modelMapper.map(travelRequestDto, TravelEntity.class);
    }

//    public TravelResponseDto fromEntity(TravelEntity travelEntity) {
//        return new TravelResponseDto(travelEntity.getStartDateTime(), travelEntity.getEndDateTime(),
//                travelEntity.getBreakfastQuantity(), travelEntity.getLunchQuantity(), travelEntity.getDinnerQuantity(),
//                travelEntity.getDailyAllowance()), travelEntity.getCostOfTotalExpense();
//    }
}