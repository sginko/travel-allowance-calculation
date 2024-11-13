package pl.sginko.travelexpense.logic.travelexpense.transportCost.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;

@AllArgsConstructor
@Service
public class TransportCostServiceImpl implements TransportCostService {
    private final TransportCostMapper transportCostMapper;

    @Override
    public TransportCostEntity createTransportCostEntity(TransportCostDto transportCostDto, TravelReportEntity travelReportEntity) {
        return transportCostMapper.toEntity(transportCostDto, travelReportEntity);
    }
}
