package pl.sginko.travelexpense.logic.travelexpense.overnightStay.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;

@Service
public class OvernightStayServiceImpl implements OvernightStayService {

    @Override
    public OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, OvernightStayEntity overnightStayEntity) {
        overnightStayEntity.calculateOvernightStayAmounts();
        return overnightStayEntity;
    }
}
