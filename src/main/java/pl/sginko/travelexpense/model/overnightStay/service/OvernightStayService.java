package pl.sginko.travelexpense.model.overnightStay.service;

import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayRequestDto;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.model.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;

public interface OvernightStayService {

    OvernightStayResponseDto calculateOvernightStay(TravelRequestDto travelRequestDto,
                                                    OvernightStayRequestDto overnightStayRequestDto,
                                                    OvernightStayEntity overnightStayEntity);
}
