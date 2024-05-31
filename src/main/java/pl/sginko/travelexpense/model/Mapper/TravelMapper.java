package pl.sginko.travelexpense.model.Mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.Dto.TravelRequestDto;
import pl.sginko.travelexpense.model.Dto.TravelResponseDto;
import pl.sginko.travelexpense.model.Entity.TravelEntity;

@Component
public class TravelMapper {

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getStartDate(), entity.getStartTime(),
                entity.getEndDate(), entity.getEndTime(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners(), entity.getTotalAmount(),
                entity.getDietAmount(), entity.getFoodAmount());
    }

    public TravelEntity toEntity(TravelRequestDto requestDto) {
        return new TravelEntity(requestDto.getStartDate(), requestDto.getStartTime(),
                requestDto.getEndDate(), requestDto.getEndTime(),
                requestDto.getDAILY_ALLOWANCE(), requestDto.getNumberOfBreakfasts(),
                requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners());
    }
}