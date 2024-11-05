package pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

@Component
public class TransportCostMapper {

    public TransportCostResponseDto toResponseDto(TransportCostEntity entity) {
        return new TransportCostResponseDto(
                entity.getId(),
                entity.getDaysForUndocumentedTransportCost(),
                entity.getUndocumentedLocalTransportCost(),
                entity.getDocumentedLocalTransportCost(),
                entity.getMeansOfTransport(),
                entity.getCostOfTravelByPublicTransport(),
                entity.getCostOfTravelByOwnTransport(),
                entity.getTransportCostAmount());
    }

    public TransportCostEntity toEntity(TransportCostDto transportCostDto, TravelEntity travelEntity) {
        return new TransportCostEntity(
                travelEntity,
                transportCostDto.getDaysForUndocumentedLocalTransportCost(),
                transportCostDto.getDocumentedLocalTransportCost(),
                transportCostDto.getMeansOfTransport(),
                transportCostDto.getCostOfTravelByPublicTransport(),
                transportCostDto.getKilometersByCarEngineUpTo900cc(),
                transportCostDto.getKilometersByCarEngineAbove900cc(),
                transportCostDto.getKilometersByMotorcycle(),
                transportCostDto.getKilometersByMoped()
        );
    }
}
