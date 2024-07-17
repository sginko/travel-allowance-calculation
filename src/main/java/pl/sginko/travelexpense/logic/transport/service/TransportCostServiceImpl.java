package pl.sginko.travelexpense.logic.transport.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TransportCostServiceImpl {

    public BigDecimal calculateUndocumentedLocalTransportCostAmount(TravelRequestDto travelRequestDto){
        travelRequestDto.
        BigDecimal dailyAllowance = travelRequestDto.getDietDto().getDailyAllowance();
        BigDecimal dailyLocalTransportCost = dailyAllowance.multiply(BigDecimal.valueOf(0.20));

        Long daysInTravel = calculateDaysInTravel(travelRequestDto);

        return dailyLocalTransportCost.multiply(BigDecimal.valueOf(daysInTravel));
    }

    private static Long calculateDaysInTravel (TravelRequestDto travelRequestDto){
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
