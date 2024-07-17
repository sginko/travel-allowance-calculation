package pl.sginko.travelexpense.logic.transport.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.transport.exception.TransportException;
import pl.sginko.travelexpense.logic.transport.model.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.transport.model.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TransportCostServiceImpl implements TransportCostService {

    public BigDecimal calculateTransportCostAmount(TravelRequestDto travelRequestDto) {
        calculateUndocumentedLocalTransportCost(travelRequestDto);
        calculateDocumentedLocalTransportCost(travelRequestDto);

        return null;
    }

    @Override
    public BigDecimal calculateUndocumentedLocalTransportCost(TravelRequestDto travelRequestDto) {
        TransportCostDto transportCostDto = travelRequestDto.getTransportCostDto();
        BigDecimal dailyAllowance = travelRequestDto.getDietDto().getDailyAllowance();
        BigDecimal dailyUndocumentedLocalTransportCost = dailyAllowance.multiply(BigDecimal.valueOf(0.20));
        Integer inputtedDaysNumberForUndocumentedLocalTransportCost = transportCostDto.getInputtedDaysNumberForUndocumentedLocalTransportCost();
        Long daysInTravel = checkDaysInTravel(travelRequestDto);

        if (inputtedDaysNumberForUndocumentedLocalTransportCost > daysInTravel) {
            throw new TransportException("The number of days entered for undocumented Local Transport Costs is greater than the number of days on the trip");
        }
        return dailyUndocumentedLocalTransportCost.multiply(BigDecimal.valueOf(daysInTravel));
    }

    @Override
    public BigDecimal calculateDocumentedLocalTransportCost(TravelRequestDto travelRequestDto) {
        TransportCostDto transportCostDto = travelRequestDto.getTransportCostDto();
        return transportCostDto.getDocumentedLocalTransportCost();
    }

    @Override
    public BigDecimal calculateCostOfTravelByPublicTransport(TravelRequestDto travelRequestDto) {
        TransportCostDto transportCostDto = travelRequestDto.getTransportCostDto();
        return transportCostDto.getCostOfTravelByPublicTransport();
    }

    @Override
    public BigDecimal calculateCostOfTravelByOwnTransport(TransportCostEntity transportCostEntity, TravelRequestDto travelRequestDto) {
        BigDecimal costByCarEngineUpTo900Cc = transportCostEntity.getCOST_BY_CAR_ENGINE_UP_TO_900_CC();
        BigDecimal costByCarEngineAboveTo900Cc = transportCostEntity.getCOST_BY_CAR_ENGINE_ABOVE_TO_900_CC();
        BigDecimal costByMotorcycle = transportCostEntity.getCOST_BY_MOTORCYCLE();
        BigDecimal costByMoped = transportCostEntity.getCOST_BY_MOPED();

        TransportCostDto transportCostDto = travelRequestDto.getTransportCostDto();
        Long kilometersByCarEngineUpTo900cc = transportCostDto.getKilometersByCarEngineUpTo900cc();
        Long kilometersByCarEngineAbove900cc = transportCostDto.getKilometersByCarEngineAbove900cc();
        Long kilometersByMotorcycle = transportCostDto.getKilometersByMotorcycle();
        Long kilometersByMoped = transportCostDto.getKilometersByMoped();

        BigDecimal amountCostByCarEngineUpTo900Cc = costByCarEngineUpTo900Cc.multiply(BigDecimal.valueOf(kilometersByCarEngineUpTo900cc));
        BigDecimal amountCostByCarEngineAboveTo900Cc = costByCarEngineAboveTo900Cc.multiply(BigDecimal.valueOf(kilometersByCarEngineAbove900cc));
        BigDecimal amountCostByMotorcycle = costByMotorcycle.multiply(BigDecimal.valueOf(kilometersByMotorcycle));
        BigDecimal amountCostByMoped = costByMoped.multiply(BigDecimal.valueOf(kilometersByMoped));

        return amountCostByCarEngineUpTo900Cc.add(amountCostByCarEngineAboveTo900Cc).add(amountCostByMotorcycle).add(amountCostByMoped);
    }

    private static Long checkDaysInTravel(TravelRequestDto travelRequestDto) {
        LocalDate startDate = travelRequestDto.getStartDate();
        LocalTime startTime = travelRequestDto.getStartTime();
        LocalDate endDate = travelRequestDto.getEndDate();
        LocalTime endTime = travelRequestDto.getEndTime();

        long hoursInTravel = Duration.between(startTime.atDate(startDate), endTime.atDate(endDate)).toHours();
        long daysInTravel = hoursInTravel / 24;
        long remainingHours = hoursInTravel % 24;

        if (remainingHours > 0) {
            daysInTravel++;
        }
        return daysInTravel;
    }
}
