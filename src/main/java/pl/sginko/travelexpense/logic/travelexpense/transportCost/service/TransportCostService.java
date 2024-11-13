package pl.sginko.travelexpense.logic.travelexpense.transportCost.service;

import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;

public interface TransportCostService {

    TransportCostEntity createTransportCostEntity(TransportCostDto transportCostDto, TravelReportEntity travelReportEntity);
}
