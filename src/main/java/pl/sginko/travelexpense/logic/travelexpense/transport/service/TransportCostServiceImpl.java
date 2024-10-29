package pl.sginko.travelexpense.logic.travelexpense.transport.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.transport.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;

@Service
public class TransportCostServiceImpl implements TransportCostService {

    @Override
    public TransportCostEntity createTransportCostEntity(TransportCostDto transportCostDto, TransportCostEntity transportCostEntity) {
        transportCostEntity.calculateTransportCostAmounts();
        return transportCostEntity;
    }
}
