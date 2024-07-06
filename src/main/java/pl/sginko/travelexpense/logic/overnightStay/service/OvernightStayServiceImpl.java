package pl.sginko.travelexpense.logic.overnightStay.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.CalculatedOvernightStay;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.exception.TravelException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class OvernightStayServiceImpl implements OvernightStayService {
    @Override
    public CalculatedOvernightStay calculateOvernightStay(TravelRequestDto travelRequestDto) {
        OvernightStayDto overnightStayDto = travelRequestDto.getOvernightStayDto();
        int quantityOfOvernightStay = getTotalQuantityOfNight(travelRequestDto);
        if (overnightStayDto.getInputQuantityOfOvernightStayWithInvoice() > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        }
        if ((overnightStayDto.getInputQuantityOfOvernightStayWithInvoice() +
                overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice()) > quantityOfOvernightStay) {
            throw new TravelException("Total input numbers of overnight stay more than total overnight stay");
        }
        if (overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice() > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        }

        BigDecimal amountOfTotalOvernightsStayWithInvoice = travelRequestDto.getOvernightStayDto().getAmountOfTotalOvernightsStayWithInvoice();
        BigDecimal dailyAllowance = BigDecimal.valueOf(45);
        BigDecimal maxAmountForOneNightWithInvoice = dailyAllowance.multiply(BigDecimal.valueOf(20));
        BigDecimal oneNightWithoutInvoice = dailyAllowance.multiply(BigDecimal.valueOf(1.5));
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice;

        amountOfTotalOvernightsStayWithoutInvoice = oneNightWithoutInvoice.multiply(BigDecimal.valueOf(overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice()));

        return CalculatedOvernightStay.builder()
                .amountOfTotalOvernightsStayWithInvoice(amountOfTotalOvernightsStayWithInvoice)
                .amountOfTotalOvernightsStayWithoutInvoice(amountOfTotalOvernightsStayWithoutInvoice)
                .overnightStayAmount(maxAmountForOneNightWithInvoice.add(amountOfTotalOvernightsStayWithoutInvoice))
                .build();
    }

    private int getTotalQuantityOfNight(TravelRequestDto travelRequestDto) {
        int night = 0;
        LocalDate startDate = travelRequestDto.getStartDate();
        LocalTime startTime = travelRequestDto.getStartTime();
        LocalDate endDate = travelRequestDto.getEndDate();
        LocalTime endTime = travelRequestDto.getEndTime();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        while (startDateTime.isBefore(endDateTime)) {
            LocalDateTime endOfCurrentNight = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);

            if (endDateTime.isBefore(endOfCurrentNight)) {
                endOfCurrentNight = endDateTime;
            }

            LocalDateTime startOfCurrentNight = startDateTime.withHour(21).withMinute(0).withSecond(0);

            if (startDateTime.isAfter(startOfCurrentNight)) {
                startOfCurrentNight = startDateTime;
            }

            if (Duration.between(startOfCurrentNight, endOfCurrentNight).toHours() >= 6) {
                night++;
            }

            startDateTime = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);
        }
        return night;
    }
}
