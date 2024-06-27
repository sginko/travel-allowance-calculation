package pl.sginko.travelexpense.model.dietExpenses.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.model.travel.service.TravelService;

import java.math.BigDecimal;

@Service
public class DietExpensesServiceImpl implements DietExpensesService {
    private final BigDecimal DAILY_ALLOWANCE = BigDecimal.valueOf(45);

    private final TravelMapper travelMapper;
    private final TravelService travelService;

    public DietExpensesServiceImpl(TravelMapper travelMapper, TravelService travelService) {
        this.travelMapper = travelMapper;
        this.travelService = travelService;
    }

    @Override
    public BigDecimal calculateDietAmount(TravelRequestDto requestDto) {
        BigDecimal dietAmount;
        long hoursInTravel = travelService.getHoursInTravel(requestDto);
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
//        travelService.updateTotalAmount();
    }

    @Override
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

    @Override
    public BigDecimal getDAILY_ALLOWANCE() {
        return DAILY_ALLOWANCE;
    }
}
