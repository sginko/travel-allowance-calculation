package pl.sginko.travelexpense.domain.travelReport.service.diet;

import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

public interface DietService {

    DietEntity createDietEntity(DietDto dietDto, TravelReportEntity travelReportEntity);
}
