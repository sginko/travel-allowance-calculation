package pl.sginko.travelexpense.logic.travelexpense.overnightStay.service;

import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;

public interface OvernightStayService {

    OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, TravelReportEntity travelReportEntity);
}
