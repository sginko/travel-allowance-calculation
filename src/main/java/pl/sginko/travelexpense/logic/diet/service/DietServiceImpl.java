package pl.sginko.travelexpense.logic.diet.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.diet.repository.DietRepository;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Service
class DietServiceImpl implements DietService {


    @Override
    @Transactional
    public BigDecimal calculateDiet(TravelRequestDto travelRequestDto) {
        if (travelRequestDto.getDietDto() == null) {
            throw new IllegalArgumentException("DietDto cannot be null");
        }

        BigDecimal dietAmount = calculateDietAmount(travelRequestDto);
        BigDecimal foodAmount = calculateFoodAmount(travelRequestDto);

        return dietAmount.add(foodAmount);
    }

    private BigDecimal calculateDietAmount(final TravelRequestDto travelRequestDto) {
        DietDto dietDto = travelRequestDto.getDietDto();
        LocalDate startDate = travelRequestDto.getStartDate();
        LocalTime startTime = travelRequestDto.getStartTime();
        LocalDate endDate = travelRequestDto.getEndDate();
        LocalTime endTime = travelRequestDto.getEndTime();
        long hoursInTravel = Duration.between(startTime.atDate(startDate), endTime.atDate(endDate)).toHours();
        BigDecimal fiftyPercentOfDailyAllowance = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.50));

        BigDecimal dietAmount = BigDecimal.ZERO;

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                dietAmount = dietAmount.add(BigDecimal.ZERO);
            } else if (hoursInTravel < 12) {
                dietAmount = dietAmount.add(fiftyPercentOfDailyAllowance);
            } else {
                dietAmount = dietAmount.add(dietDto.getDailyAllowance());
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            BigDecimal totalAmountForFullDays = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours == 0) {
                dietAmount = dietAmount.add(totalAmountForFullDays.add(BigDecimal.ZERO));
            } else if (remainingHours < 8) {
                dietAmount = dietAmount.add(totalAmountForFullDays.add(fiftyPercentOfDailyAllowance));
            } else {
                dietAmount = dietAmount.add(totalAmountForFullDays.add(dietDto.getDailyAllowance()));
            }
        }
        return dietAmount;
    }

    private BigDecimal calculateFoodAmount(TravelRequestDto travelRequestDto) {
        DietDto dietDto = travelRequestDto.getDietDto();
        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(dietDto.getNumberOfBreakfasts()).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(dietDto.getNumberOfLunches()).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(dietDto.getNumberOfDinners()).multiply(twentyFivePercentOfDailyAllowance);

        return totalFoodExpenses.add(breakfastCost).add(lunchCost).add(dinnerCost).negate();
    }
}
