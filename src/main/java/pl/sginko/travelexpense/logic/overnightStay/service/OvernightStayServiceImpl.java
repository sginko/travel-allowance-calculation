package pl.sginko.travelexpense.logic.overnightStay.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.diet.model.dto.DietDto;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class OvernightStayServiceImpl implements OvernightStayService {

    @Override
    public BigDecimal calculateOvernightStay(final TravelRequestDto travelRequestDto) {
        BigDecimal amountOfOvernightStayWithoutInvoice = calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);
        BigDecimal amountOfOvernightStayWithInvoice = calculateAmountOfOvernightStayWithInvoice(travelRequestDto);
        return amountOfOvernightStayWithInvoice.add(amountOfOvernightStayWithoutInvoice);
    }

    @Override
    public BigDecimal calculateAmountOfOvernightStayWithoutInvoice(final TravelRequestDto travelRequestDto) {
        OvernightStayDto overnightStayDto = travelRequestDto.getOvernightStayDto();
        DietDto dietDto = travelRequestDto.getDietDto();
        Integer quantityOfOvernightStay = calculateQuantityOfOvernightStay(travelRequestDto);
        BigDecimal dailyAllowance = dietDto.getDailyAllowance();
        BigDecimal oneNightWithoutInvoice = dailyAllowance.multiply(BigDecimal.valueOf(1.5));
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice = BigDecimal.ZERO;

        Integer totalInputQuantityOfOvernightStay = calculateTotalInputQuantityOfOvernightStay(travelRequestDto);
        Integer inputQuantityOfOvernightStayWithInvoice = overnightStayDto.getInputQuantityOfOvernightStayWithInvoice();
        Integer inputQuantityOfOvernightStayWithoutInvoice = overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice();

        if (inputQuantityOfOvernightStayWithInvoice > quantityOfOvernightStay ||
                inputQuantityOfOvernightStayWithoutInvoice > quantityOfOvernightStay ||
                totalInputQuantityOfOvernightStay > quantityOfOvernightStay) {
            throw new TravelException("Quantity numbers of nights more than nights in travel");
        }

        amountOfTotalOvernightsStayWithoutInvoice = oneNightWithoutInvoice.multiply(BigDecimal.valueOf(inputQuantityOfOvernightStayWithoutInvoice));
        return amountOfTotalOvernightsStayWithoutInvoice;
    }

    @Override
    public BigDecimal calculateAmountOfOvernightStayWithInvoice(final TravelRequestDto travelRequestDto) {
        OvernightStayDto overnightStayDto = travelRequestDto.getOvernightStayDto();

        Integer quantityOfOvernightStay = calculateQuantityOfOvernightStay(travelRequestDto);
        Integer totalInputQuantityOfOvernightStay = calculateTotalInputQuantityOfOvernightStay(travelRequestDto);
        Integer inputQuantityOfOvernightStayWithInvoice = overnightStayDto.getInputQuantityOfOvernightStayWithInvoice();
        Integer inputQuantityOfOvernightStayWithoutInvoice = overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice();

        if (inputQuantityOfOvernightStayWithInvoice > quantityOfOvernightStay ||
                inputQuantityOfOvernightStayWithoutInvoice > quantityOfOvernightStay ||
                totalInputQuantityOfOvernightStay > quantityOfOvernightStay) {
            throw new TravelException("Quantity numbers of nights more than nights in travel");
        }

        DietDto dietDto = travelRequestDto.getDietDto();

        BigDecimal dailyAllowance = dietDto.getDailyAllowance();
        BigDecimal maxAmountForOneNightWithInvoice = dailyAllowance.multiply(BigDecimal.valueOf(20));

        BigDecimal amountOfTotalOvernightsStayWithInvoice = overnightStayDto.getAmountOfTotalOvernightsStayWithInvoice();
        return amountOfTotalOvernightsStayWithInvoice;
    }

    @Override
    public Integer calculateQuantityOfOvernightStay(final TravelRequestDto travelRequestDto) {
        Integer quantityOfOvernightStay = 0;
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
                quantityOfOvernightStay++;
            }

            startDateTime = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);
        }
        return quantityOfOvernightStay;
    }

    @Override
    public Integer calculateTotalInputQuantityOfOvernightStay(final TravelRequestDto travelRequestDto) {
        OvernightStayDto overnightStayDto = travelRequestDto.getOvernightStayDto();
        Integer totalInputQuantityOfOvernightStay = overnightStayDto.getInputQuantityOfOvernightStayWithInvoice() +
                overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice();
        return totalInputQuantityOfOvernightStay;
    }
}
