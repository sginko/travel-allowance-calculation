package pl.sginko.travelexpense.domain.travelReport.service.diet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.mapper.DietMapper;

@AllArgsConstructor
@Service
public class DietServiceImpl implements DietService {
    private final DietMapper dietMapper;

    @Override
    public DietEntity createDietEntity(DietDto dietDto, TravelReportEntity travelReportEntity) {
        DietEntity dietEntity = dietMapper.toEntity(dietDto, travelReportEntity);
        return dietEntity;
    }
}
