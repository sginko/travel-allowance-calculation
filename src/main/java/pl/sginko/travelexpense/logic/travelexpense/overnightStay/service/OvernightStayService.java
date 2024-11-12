package pl.sginko.travelexpense.logic.travelexpense.overnightStay.service;

import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

public interface OvernightStayService {

    OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, TravelEntity travelEntity);
}
