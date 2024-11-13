package pl.sginko.travelexpense.logic.travelexpense.diet.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietEditDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;

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

    public DietEntity toEntity(DietDto dietDto, TravelReportEntity travelReportEntity) {
        return new DietEntity(
                travelReportEntity,
                dietDto.getDailyAllowance(),
                dietDto.getNumberOfBreakfasts(),
                dietDto.getNumberOfLunches(),
                dietDto.getNumberOfDinners());
    }

    public DietEditDto toDietEditDto(DietEntity entity) {
        return new DietEditDto(
                entity.getDailyAllowance(),
                entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(),
                entity.getNumberOfDinners());
    }
}
