package pl.sginko.travelexpense.logic.travelexpense.transport.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.transport.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transport.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class TransportCostServiceImpl implements TransportCostService {
    private final TransportCostMapper transportCostMapper;

    @Override
    public TransportCostEntity createTransportCostEntity(TravelEntity travelEntity, TransportCostDto transportCostDto) {
        return transportCostMapper.toEntity(transportCostDto, travelEntity);
    }
}
