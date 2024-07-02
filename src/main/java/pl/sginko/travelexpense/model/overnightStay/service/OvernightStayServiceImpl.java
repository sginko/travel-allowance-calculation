package pl.sginko.travelexpense.model.overnightStay.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.model.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.model.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.model.overnightStay.repository.OvernightStayRepository;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayRequestDto;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

@Service
public class OvernightStayServiceImpl implements OvernightStayService {
    private final OvernightStayRepository overnightStayRepository;
    private final OvernightStayMapper overnightStayMapper;

    public OvernightStayServiceImpl(OvernightStayRepository overnightStayRepository, OvernightStayMapper overnightStayMapper) {
        this.overnightStayRepository = overnightStayRepository;
        this.overnightStayMapper = overnightStayMapper;
    }

    @Override
    @Transactional
    public OvernightStayResponseDto calculateOvernightStay(OvernightStayRequestDto requestDto, TravelEntity travelEntity) {
        OvernightStayEntity entity = overnightStayMapper.toEntity(requestDto, travelEntity);
        overnightStayRepository.save(entity);
        return overnightStayMapper.toResponseDto(entity);
    }
}
