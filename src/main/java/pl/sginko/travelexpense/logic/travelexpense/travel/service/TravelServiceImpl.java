package pl.sginko.travelexpense.logic.travelexpense.travel.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transport.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;

@AllArgsConstructor
@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;
    private final TransportCostMapper transportCostMapper;

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(final TravelRequestDto travelRequestDto) {
        TravelEntity travelEntity = travelMapper.toTravelEntity(travelRequestDto);

        DietEntity dietEntity = dietMapper.toEntity(travelRequestDto.getDietDto(), travelEntity);
        dietEntity.calculateDietAmounts();
        travelEntity.updateDietEntity(dietEntity);

        OvernightStayEntity overnightStayEntity = overnightStayMapper.toEntity(travelRequestDto.getOvernightStayDto(), travelEntity);
        overnightStayEntity.calculateOvernightStayAmounts();
        travelEntity.updateOvernightStayEntity(overnightStayEntity);

        TransportCostEntity transportCostEntity = transportCostMapper.toEntity(travelRequestDto.getTransportCostDto(), travelEntity);
        transportCostEntity.calculateTransportCostAmounts();
        travelEntity.updateTransportCostEntity(transportCostEntity);

        travelEntity.updateTotalAmount();

        travelRepository.save(travelEntity);

        return travelMapper.toResponseDto(travelEntity);
    }
}
