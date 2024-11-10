package pl.sginko.travelexpense.domen.travelexpense.overnightStay.service;

import pl.sginko.travelexpense.domen.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.domen.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelEntity;

public interface OvernightStayService {

    OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, TravelEntity travelEntity);
}
