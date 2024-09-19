package pl.sginko.travelexpense.logic.transport.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.transport.exception.TransportException;
import pl.sginko.travelexpense.logic.transport.model.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TransportCostServiceImpl implements TransportCostService {
    private final BigDecimal COST_BY_CAR_ENGINE_UP_TO_900_CC = BigDecimal.valueOf(0.89);
    private final BigDecimal COST_BY_CAR_ENGINE_ABOVE_TO_900_CC = BigDecimal.valueOf(1.15);
    private final BigDecimal COST_BY_MOTORCYCLE = BigDecimal.valueOf(0.69);
    private final BigDecimal COST_BY_MOPED = BigDecimal.valueOf(0.42);

    @Override
    public BigDecimal calculateTransportCostAmount(TravelRequestDto travelRequestDto) {
        BigDecimal undocumentedLocalTransportCost = calculateUndocumentedLocalTransportCost(travelRequestDto);
        BigDecimal documentedLocalTransportCost = calculateDocumentedLocalTransportCost(travelRequestDto);
        BigDecimal costOfTravelByPublicTransport = calculateCostOfTravelByPublicTransport(travelRequestDto);
        BigDecimal costOfTravelByOwnTransport = calculateCostOfTravelByOwnTransport(travelRequestDto);
        return undocumentedLocalTransportCost.add(documentedLocalTransportCost).
                add(costOfTravelByPublicTransport).add(costOfTravelByOwnTransport);
    }

    @Override
    public BigDecimal calculateTotalCostOfTravelByOwnAndPublicTransport(TravelRequestDto travelRequestDto) {
        BigDecimal costOfTravelByPublicTransport = calculateCostOfTravelByPublicTransport(travelRequestDto);
        BigDecimal costOfTravelByOwnTransport = calculateCostOfTravelByOwnTransport(travelRequestDto);
        return costOfTravelByPublicTransport.add(costOfTravelByOwnTransport);
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
        return dailyUndocumentedLocalTransportCost.multiply(BigDecimal.valueOf(inputtedDaysNumberForUndocumentedLocalTransportCost));
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
    public BigDecimal calculateCostOfTravelByOwnTransport(TravelRequestDto travelRequestDto) {
        TransportCostDto transportCostDto = travelRequestDto.getTransportCostDto();
        Long kilometersByCarEngineUpTo900cc = transportCostDto.getKilometersByCarEngineUpTo900cc();
        Long kilometersByCarEngineAbove900cc = transportCostDto.getKilometersByCarEngineAbove900cc();
        Long kilometersByMotorcycle = transportCostDto.getKilometersByMotorcycle();
        Long kilometersByMoped = transportCostDto.getKilometersByMoped();

        BigDecimal amountCostByCarEngineUpTo900Cc = COST_BY_CAR_ENGINE_UP_TO_900_CC.multiply(BigDecimal.valueOf(kilometersByCarEngineUpTo900cc));
        BigDecimal amountCostByCarEngineAboveTo900Cc = COST_BY_CAR_ENGINE_ABOVE_TO_900_CC.multiply(BigDecimal.valueOf(kilometersByCarEngineAbove900cc));
        BigDecimal amountCostByMotorcycle = COST_BY_MOTORCYCLE.multiply(BigDecimal.valueOf(kilometersByMotorcycle));
        BigDecimal amountCostByMoped = COST_BY_MOPED.multiply(BigDecimal.valueOf(kilometersByMoped));

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
