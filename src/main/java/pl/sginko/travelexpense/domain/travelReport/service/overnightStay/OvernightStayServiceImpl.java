package pl.sginko.travelexpense.domain.travelReport.service.overnightStay;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayDto;
import pl.sginko.travelexpense.domain.travelReport.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.mapper.OvernightStayMapper;

@AllArgsConstructor
@Service
public class OvernightStayServiceImpl implements OvernightStayService {
    private final OvernightStayMapper overnightStayMapper;

    @Override
    public OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, TravelReportEntity travelReportEntity) {
        OvernightStayEntity overnightStayEntity = overnightStayMapper.toEntity(overnightStayDto, travelReportEntity);
        return overnightStayEntity;
    }
}
