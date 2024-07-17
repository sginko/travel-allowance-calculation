package pl.sginko.travelexpense.logic.transport.service;

import pl.sginko.travelexpense.logic.transport.model.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface TransportCostService {

    BigDecimal calculateUndocumentedLocalTransportCost(TravelRequestDto travelRequestDto);

    BigDecimal calculateDocumentedLocalTransportCost(TravelRequestDto travelRequestDto);

    BigDecimal calculateCostOfTravelByPublicTransport(TravelRequestDto travelRequestDto);

    BigDecimal calculateCostOfTravelByOwnTransport(TransportCostEntity transportCostEntity, TravelRequestDto travelRequestDto);
}
