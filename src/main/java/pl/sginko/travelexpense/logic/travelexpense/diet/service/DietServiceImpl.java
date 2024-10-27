package pl.sginko.travelexpense.logic.travelexpense.diet.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
class DietServiceImpl implements DietService {

    @Override
    public BigDecimal calculateDiet(final TravelRequestDto travelRequestDto) {
        BigDecimal dietAmount = calculateDietAmount(travelRequestDto);
        BigDecimal foodAmount = calculateFoodAmount(travelRequestDto);
        return dietAmount.add(foodAmount);
    }

    @Override
    public BigDecimal calculateDietAmount(final TravelRequestDto travelRequestDto) {
        LocalDate startDate = travelRequestDto.getStartDate();
        LocalTime startTime = travelRequestDto.getStartTime();
        LocalDate endDate = travelRequestDto.getEndDate();
        LocalTime endTime = travelRequestDto.getEndTime();
        long hoursInTravel = Duration.between(startTime.atDate(startDate), endTime.atDate(endDate)).toHours();

        BigDecimal dailyAllowance = travelRequestDto.getDietDto().getDailyAllowance();
        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.50));
        BigDecimal dietAmount = BigDecimal.ZERO;

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                dietAmount = dietAmount.add(BigDecimal.ZERO);
            } else if (hoursInTravel < 12) {
                dietAmount = dietAmount.add(fiftyPercentOfDailyAllowance);
            } else {
                dietAmount = dietAmount.add(dailyAllowance);
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            BigDecimal totalAmountForFullDays = dailyAllowance.multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours == 0) {
                dietAmount = dietAmount.add(totalAmountForFullDays.add(BigDecimal.ZERO));
            } else if (remainingHours < 8) {
                dietAmount = dietAmount.add(totalAmountForFullDays.add(fiftyPercentOfDailyAllowance));
            } else {
                dietAmount = dietAmount.add(totalAmountForFullDays.add(dailyAllowance));
            }
        }
        return dietAmount;
    }

    @Override
    public BigDecimal calculateFoodAmount(final TravelRequestDto travelRequestDto) {
        DietDto dietDto = travelRequestDto.getDietDto();
        BigDecimal foodAmount = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(dietDto.getNumberOfBreakfasts()).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(dietDto.getNumberOfLunches()).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(dietDto.getNumberOfDinners()).multiply(twentyFivePercentOfDailyAllowance);

        return foodAmount.add(breakfastCost).add(lunchCost).add(dinnerCost).negate();
    }
}
