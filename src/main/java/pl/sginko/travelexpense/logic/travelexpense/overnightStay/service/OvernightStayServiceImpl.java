package pl.sginko.travelexpense.logic.travelexpense.overnightStay.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

@AllArgsConstructor
@Service
public class OvernightStayServiceImpl implements OvernightStayService {
    private final OvernightStayMapper overnightStayMapper;

    @Override
    public OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, TravelEntity travelEntity) {
        OvernightStayEntity overnightStayEntity = overnightStayMapper.toEntity(overnightStayDto, travelEntity);
        return overnightStayEntity;
    }
}
