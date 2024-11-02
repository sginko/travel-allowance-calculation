package pl.sginko.travelexpense.logic.travelexpense.transport.service;

import pl.sginko.travelexpense.logic.travelexpense.transport.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

public interface TransportCostService {

    TransportCostEntity createTransportCostEntity(TransportCostDto transportCostDto, TravelEntity travelEntity);
}
