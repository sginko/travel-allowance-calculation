package pl.sginko.travelexpense.model.dietExpenses.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.dietExpenses.dto.DietExpensesRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.service.TravelReadService;

import java.math.BigDecimal;

@Service
public class DietExpensesServiceImpl implements DietExpensesService {
    private final TravelReadService travelReadService;

    public DietExpensesServiceImpl(TravelReadService travelReadService) {
        this.travelReadService = travelReadService;
    }

    @Override
    public BigDecimal calculateDietAmount(DietExpensesRequestDto dietRequestDto, TravelRequestDto travelRequestDto) {
        BigDecimal dietAmount;
        long hoursInTravel = travelReadService.getHoursInTravel(travelRequestDto);
        BigDecimal fiftyPercentOfDailyAllowance = dietRequestDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.50));

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                dietAmount = BigDecimal.ZERO;
            } else if (hoursInTravel < 12) {
                dietAmount = fiftyPercentOfDailyAllowance;
            } else {
                dietAmount = dietRequestDto.getDailyAllowance();
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            BigDecimal totalAmountForFullDays = dietRequestDto.getDailyAllowance().multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours == 0) {
                dietAmount = totalAmountForFullDays.add(BigDecimal.ZERO);
            } else if (remainingHours < 8) {
                dietAmount = totalAmountForFullDays.add(fiftyPercentOfDailyAllowance);
            } else {
                dietAmount = totalAmountForFullDays.add(dietRequestDto.getDailyAllowance());
            }
        }
        return dietAmount;
        travelReadService.updateTotalAmount();
    }

    @Override
    public BigDecimal calculateFoodAmount(DietExpensesRequestDto dietRequestDto) {
        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = dietRequestDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = dietRequestDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(dietRequestDto.getNumberOfBreakfasts()).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(dietRequestDto.getNumberOfLunches()).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(dietRequestDto.getNumberOfDinners()).multiply(twentyFivePercentOfDailyAllowance);

        BigDecimal foodAmount = totalFoodExpenses.add(breakfastCost).add(lunchCost).add(dinnerCost).negate();
        return foodAmount;

//        updateTotalAmount();
    }
}
