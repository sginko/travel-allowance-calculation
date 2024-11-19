package pl.sginko.travelexpense.domain.travelReport.service.transportCost;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TransportCostEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.mapper.TransportCostMapper;

@AllArgsConstructor
@Service
public class TransportCostServiceImpl implements TransportCostService {
    private final TransportCostMapper transportCostMapper;

    @Override
    public TransportCostEntity createTransportCostEntity(TransportCostDto transportCostDto, TravelReportEntity travelReportEntity) {
        return transportCostMapper.toEntity(transportCostDto, travelReportEntity);
    }
}
