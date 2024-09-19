package pl.sginko.travelexpense.logic.transport.service;

import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface TransportCostService {

    BigDecimal calculateTransportCostAmount(TravelRequestDto travelRequestDto);

    BigDecimal calculateTotalCostOfTravelByOwnAndPublicTransport(TravelRequestDto travelRequestDto);

    BigDecimal calculateUndocumentedLocalTransportCost(TravelRequestDto travelRequestDto);

    BigDecimal calculateDocumentedLocalTransportCost(TravelRequestDto travelRequestDto);

    BigDecimal calculateCostOfTravelByPublicTransport(TravelRequestDto travelRequestDto);

    BigDecimal calculateCostOfTravelByOwnTransport(TravelRequestDto travelRequestDto);
}
