package pl.sginko.travelexpense.logic.transport.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.transport.model.dto.TransportCostResponseDto;
import pl.sginko.travelexpense.logic.transport.model.entity.TransportCostEntity;

@Component
public class TransportCostMapper {

    public TransportCostResponseDto toResponseDto(TransportCostEntity entity) {
        return new TransportCostResponseDto(entity.getId(), entity.getInputtedDaysNumberForUndocumentedTransportCost(),
                entity.getUndocumentedLocalTransportCost(), entity.getDocumentedLocalTransportCost(), entity.getMeansOfTransport(),
                entity.getCostOfTravelByPublicTransport(), entity.getCostOfTravelByOwnTransport(), entity.getTransportCostAmount());
    }
}
