package pl.sginko.travelexpense.logic.travelexpense.transportCost.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

@AllArgsConstructor
@Service
public class TransportCostServiceImpl implements TransportCostService {
    private final TransportCostMapper transportCostMapper;

    @Override
    public TransportCostEntity createTransportCostEntity(TransportCostDto transportCostDto, TravelEntity travelEntity) {
        return transportCostMapper.toEntity(transportCostDto, travelEntity);
    }
}
