package pl.sginko.travelexpense.domain.travelReport.service.overnightStay;

import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayDto;
import pl.sginko.travelexpense.domain.travelReport.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

public interface OvernightStayService {

    OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, TravelReportEntity travelReportEntity);
}
