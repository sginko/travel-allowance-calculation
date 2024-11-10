package pl.sginko.travelexpense.domen.travelexpense.transportCost.service;

import pl.sginko.travelexpense.domen.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.domen.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelEntity;

public interface TransportCostService {

    TransportCostEntity createTransportCostEntity(TransportCostDto transportCostDto, TravelEntity travelEntity);
}
