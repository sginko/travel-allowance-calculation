package pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostEditDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;

@Component
public class TransportCostMapper {

    public TransportCostResponseDto toResponseDto(TransportCostEntity entity) {
        return new TransportCostResponseDto(
                entity.getId(),
                entity.getDaysForUndocumentedLocalTransportCost(),
                entity.getUndocumentedLocalTransportCost(),
                entity.getDocumentedLocalTransportCost(),
                entity.getMeansOfTransport(),
                entity.getCostOfTravelByPublicTransport(),
                entity.getCostOfTravelByOwnTransport(),
                entity.getTransportCostAmount());
    }

    public TransportCostEntity toEntity(TransportCostDto transportCostDto, TravelReportEntity travelReportEntity) {
        return new TransportCostEntity(
                travelReportEntity,
                transportCostDto.getDaysForUndocumentedLocalTransportCost(),
                transportCostDto.getDocumentedLocalTransportCost(),
                transportCostDto.getMeansOfTransport(),
                transportCostDto.getCostOfTravelByPublicTransport(),
                transportCostDto.getKilometersByCarEngineUpTo900cc(),
                transportCostDto.getKilometersByCarEngineAbove900cc(),
                transportCostDto.getKilometersByMotorcycle(),
                transportCostDto.getKilometersByMoped());
    }

    public TransportCostEditDto toTransportCosEditDto(TransportCostEntity entity) {
        return new TransportCostEditDto(
                entity.getDaysForUndocumentedLocalTransportCost(),
                entity.getDocumentedLocalTransportCost(),
                entity.getMeansOfTransport(),
                entity.getCostOfTravelByPublicTransport(),
                entity.getKilometersByCarEngineUpTo900cc(),
                entity.getKilometersByCarEngineAbove900cc(),
                entity.getKilometersByMotorcycle(),
                entity.getKilometersByMoped());
    }
}
