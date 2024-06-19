package pl.sginko.travelexpense.model.travel.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

@Component
public class TravelMapper {

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getStartDate(), entity.getStartTime(),
                entity.getEndDate(), entity.getEndTime(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners(), entity.getTotalAmount(),
                entity.getDietAmount(), entity.getFoodAmount(), entity.getEmployeeEntity().getPesel());
    }

    public TravelEntity toEntity(TravelRequestDto requestDto) {
        return new TravelEntity(requestDto.getStartDate(), requestDto.getStartTime(),
                requestDto.getEndDate(), requestDto.getEndTime(),
                requestDto.getDAILY_ALLOWANCE(), requestDto.getNumberOfBreakfasts(),
                requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners());
    }
}
