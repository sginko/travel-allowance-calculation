package pl.sginko.travelexpense.logic.travelexpense.overnightStay.service;

import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;

public interface OvernightStayService {

    OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, OvernightStayEntity overnightStayEntity);
}
