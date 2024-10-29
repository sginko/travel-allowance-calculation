package pl.sginko.travelexpense.logic.travelexpense.transport.service;

import pl.sginko.travelexpense.logic.travelexpense.transport.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface TransportCostService {

    TransportCostEntity createTransportCostEntity(TransportCostDto transportCostDto, TransportCostEntity transportCostEntity);
}
