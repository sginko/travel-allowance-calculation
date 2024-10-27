package pl.sginko.travelexpense.logic.travelexpense.transport.service;

import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface TransportCostService {

    BigDecimal calculateTransportCostAmount(TravelRequestDto travelRequestDto);

    BigDecimal calculateTotalCostOfTravelByOwnAndPublicTransport(TravelRequestDto travelRequestDto);

    BigDecimal calculateUndocumentedLocalTransportCost(TravelRequestDto travelRequestDto);

    BigDecimal calculateDocumentedLocalTransportCost(TravelRequestDto travelRequestDto);

    BigDecimal calculateCostOfTravelByPublicTransport(TravelRequestDto travelRequestDto);

    BigDecimal calculateCostOfTravelByOwnTransport(TravelRequestDto travelRequestDto);
}
