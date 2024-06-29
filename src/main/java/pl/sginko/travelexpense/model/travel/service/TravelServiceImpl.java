package pl.sginko.travelexpense.model.travel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.model.travel.TravelException;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.model.travel.repository.TravelRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TravelServiceImpl implements TravelService {
    private final BigDecimal DAILY_ALLOWANCE = BigDecimal.valueOf(45);

    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;

    public TravelServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
    }

    public void addTravel(TravelRequestDto requestDto){
        travelRepository.save(travelMapper.toEntity(requestDto));
    }

    public BigDecimal calculateDietAmount(TravelRequestDto requestDto) {
        BigDecimal dietAmount;
        long hoursInTravel = Duration.between(requestDto.getStartTime().atDate(requestDto.getStartDate()),
                requestDto.getEndTime().atDate(requestDto.getEndDate())).toHours();
        BigDecimal fiftyPercentOfDailyAllowance = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.50));

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                dietAmount = BigDecimal.ZERO;
            } else if (hoursInTravel < 12) {
                dietAmount = fiftyPercentOfDailyAllowance;
            } else {
                dietAmount = DAILY_ALLOWANCE;
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            BigDecimal totalAmountForFullDays = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours == 0) {
                dietAmount = totalAmountForFullDays.add(BigDecimal.ZERO);
            } else if (remainingHours < 8) {
                dietAmount = totalAmountForFullDays.add(fiftyPercentOfDailyAllowance);
            } else {
                dietAmount = totalAmountForFullDays.add(DAILY_ALLOWANCE);
            }
        }
        return dietAmount;
//        updateTotalAmount();
    }

    public BigDecimal calculateFoodAmount(TravelRequestDto requestDto) {
        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(requestDto.getNumberOfBreakfasts()).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(requestDto.getNumberOfLunches()).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(requestDto.getNumberOfDinners()).multiply(twentyFivePercentOfDailyAllowance);

        BigDecimal foodAmount = totalFoodExpenses.add(breakfastCost).add(lunchCost).add(dinnerCost).negate();
        return foodAmount;
//        updateTotalAmount();
    }

    public BigDecimal calculateOvernightStayAmount(TravelRequestDto requestDto) {
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice;
        Integer totalInputQuantityOfOvernightStay;

        Integer quantityOfOvernightStay = getTotalQuantityOfNight(requestDto);

        BigDecimal oneNightWithInvoice = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(20));
        BigDecimal oneNightWithoutInvoice = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(1.5));


        if (requestDto.getInputQuantityOfOvernightStayWithoutInvoice() > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        } else {
            amountOfTotalOvernightsStayWithoutInvoice = oneNightWithoutInvoice.multiply(BigDecimal.valueOf(requestDto.getInputQuantityOfOvernightStayWithoutInvoice()));
        }

        if (requestDto.getInputQuantityOfOvernightStayWithInvoice() > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        }

        if ((requestDto.getInputQuantityOfOvernightStayWithInvoice() + requestDto.getInputQuantityOfOvernightStayWithoutInvoice()) > quantityOfOvernightStay) {
            throw new TravelException("Total input numbers of overnight stay more than total overnight stay");
        }
        totalInputQuantityOfOvernightStay = requestDto.getInputQuantityOfOvernightStayWithInvoice() + requestDto.getInputQuantityOfOvernightStayWithoutInvoice();
        BigDecimal overnightStayAmount = amountOfTotalOvernightsStayWithoutInvoice.add(requestDto.getAmountOfTotalOvernightsStayWithInvoice());

        return overnightStayAmount;
//            updateTotalAmount();
    }

    private Integer getTotalQuantityOfNight(TravelRequestDto requestDto) {
        int night = 0;
        LocalDateTime startDateTime = LocalDateTime.of(requestDto.getStartDate(), requestDto.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(requestDto.getEndDate(), requestDto.getEndTime());

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

    private BigDecimal calculateTotalAmount(TravelRequestDto requestDto) {
        BigDecimal totalAmount = (calculateDietAmount(requestDto).add(calculateFoodAmount(requestDto))
                .add(calculateOvernightStayAmount(requestDto))).subtract(requestDto.getAdvancePayment());
        return totalAmount;
    }

    @Override
    public TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto) {
        TravelEntity entity = travelMapper.toEntity(requestDto);
        entity.calculateDietAmount(requestDto);
        BigDecimal bigDecimal1 = calculateFoodAmount(requestDto);
        BigDecimal bigDecimal2 = calculateOvernightStayAmount(requestDto);
        addTravel(requestDto);
        return travelMapper.toResponseDto(entity);
    }
}
