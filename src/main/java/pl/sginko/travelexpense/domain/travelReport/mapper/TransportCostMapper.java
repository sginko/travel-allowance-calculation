package pl.sginko.travelexpense.domain.travelReport.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TransportCostEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

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
