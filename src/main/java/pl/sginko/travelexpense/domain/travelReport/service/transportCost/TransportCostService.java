package pl.sginko.travelexpense.domain.travelReport.service.transportCost;

import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TransportCostEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

public interface TransportCostService {

    TransportCostEntity createTransportCostEntity(TransportCostDto transportCostDto, TravelReportEntity travelReportEntity);
}
