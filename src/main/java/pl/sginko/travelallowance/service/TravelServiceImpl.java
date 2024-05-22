package pl.sginko.travelallowance.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;
import pl.sginko.travelallowance.model.Entity.TravelEntity;
import pl.sginko.travelallowance.model.Mapper.TravelMapper;
import pl.sginko.travelallowance.repository.TravelRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;

    public TravelServiceImpl(TravelRepository travelRepository, ModelMapper modelMapper, TravelMapper travelMapper) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
    }

    @Override
    public TravelResponseDto addTravelExpenses(TravelRequestDto travelRequestDto) {
        BigDecimal totalCostOfTravelExpenses = getTotalCostOfTravelExpenses(travelRequestDto);
        TravelEntity entityAfterCalculation = travelMapper.toEntityAfterCalculation(travelRequestDto, totalCostOfTravelExpenses);
        travelRepository.save(entityAfterCalculation);
        TravelResponseDto travelResponseDto = travelMapper.fromEntity(entityAfterCalculation);
        return travelResponseDto;
    }

    @Override
    public TravelResponseDto findTravelExpensesForTravelRequest(Long id) {

        return null;
    }


    private BigDecimal getTotalCostOfTravelExpenses(TravelRequestDto travelRequestDto) {
        BigDecimal totalTravelExpenses = calculationOfTravelExpenses(travelRequestDto.getDAILY_ALLOWANCE(),
                travelRequestDto.getStartDateTime(), travelRequestDto.getEndDateTime());

        BigDecimal totalFoodExpenses = calculationOfFoodExpenses(travelRequestDto.getBreakfastQuantity(), travelRequestDto.getLunchQuantity(),
                travelRequestDto.getDinnerQuantity(), travelRequestDto.getDAILY_ALLOWANCE());

        return totalTravelExpenses.subtract(totalFoodExpenses);
    }

    private Long getHourInTravel(LocalDateTime startDay, LocalDateTime endDay) {
        Duration duration = Duration.between(startDay, endDay);
        return duration.toHours();
    }

    private BigDecimal calculationOfTravelExpenses(BigDecimal dailyAllowance, LocalDateTime startDay, LocalDateTime endDay) {
        long hoursInTravel = getHourInTravel(startDay, endDay);
        BigDecimal fiftyPercentOfDailyAllowance = BigDecimal.valueOf(50 / 100).multiply(dailyAllowance);

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                return new BigDecimal(0);
            }
            if (hoursInTravel <= 12) {
                return fiftyPercentOfDailyAllowance;
            }
            return dailyAllowance;
        } else {
            long hoursInTravelLessThanDay = hoursInTravel % 24;
            long fullDays = (hoursInTravel - hoursInTravelLessThanDay) / 24;
            BigDecimal totalAmountForFullDays = BigDecimal.valueOf(fullDays).multiply(dailyAllowance);

            if (hoursInTravelLessThanDay < 8) {
                return totalAmountForFullDays.add(fiftyPercentOfDailyAllowance);
            }
            return totalAmountForFullDays.add(dailyAllowance);
        }
    }

    private BigDecimal calculationOfFoodExpenses(Integer breakfastQuantity, Integer lunchQuantity, Integer dinnerQuantity, BigDecimal dailyAllowance) {
        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = BigDecimal.valueOf(50 / 100.0).multiply(dailyAllowance);
        BigDecimal twentyFivePercentOfDailyAllowance = BigDecimal.valueOf(25 / 100.0).multiply(dailyAllowance);

        BigDecimal moneyCostForBreakfast = BigDecimal.valueOf(breakfastQuantity).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal moneyCostForLunch = BigDecimal.valueOf(lunchQuantity).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal moneyCostForDinner = BigDecimal.valueOf(dinnerQuantity).multiply(twentyFivePercentOfDailyAllowance);

        return totalFoodExpenses.add(moneyCostForBreakfast).add(moneyCostForLunch).add(moneyCostForDinner);
    }
}