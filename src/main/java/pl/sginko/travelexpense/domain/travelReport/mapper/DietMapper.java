package pl.sginko.travelexpense.domain.travelReport.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietDto;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

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
