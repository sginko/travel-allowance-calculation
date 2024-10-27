package pl.sginko.travelexpense.logic.travelexpense.transport.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.travelexpense.transport.dto.TransportCostResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;

@Component
public class TransportCostMapper {

    public TransportCostResponseDto toResponseDto(TransportCostEntity entity) {
        return new TransportCostResponseDto(entity.getId(), entity.getInputtedDaysNumberForUndocumentedTransportCost(),
                entity.getUndocumentedLocalTransportCost(), entity.getDocumentedLocalTransportCost(), entity.getMeansOfTransport(),
                entity.getCostOfTravelByPublicTransport(), entity.getCostOfTravelByOwnTransport(), entity.getTransportCostAmount());
    }
}
