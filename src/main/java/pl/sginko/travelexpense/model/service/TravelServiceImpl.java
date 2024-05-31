package pl.sginko.travelexpense.model.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.Dto.TravelRequestDto;
import pl.sginko.travelexpense.model.Dto.TravelResponseDto;
import pl.sginko.travelexpense.model.Entity.TravelEntity;
import pl.sginko.travelexpense.model.Mapper.TravelMapper;
import pl.sginko.travelexpense.model.repository.TravelRepository;

@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;

    public TravelServiceImpl(TravelRepository travelRepository, ModelMapper modelMapper, TravelMapper travelMapper) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
    }

    @Override
    public TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto) {
        TravelEntity entityAfterCalculation = travelMapper.toEntityAfterCalculation(travelRequestDto);
        entityAfterCalculation.calculateDiet();
        entityAfterCalculation.calculateFood();
        travelRepository.save(entityAfterCalculation);
        TravelResponseDto travelResponseDto = travelMapper.fromEntity(entityAfterCalculation);
        return travelResponseDto;
    }


//    private BigDecimal getTotalCostOfTravelExpenses(TravelRequestDto travelRequestDto) {
//        BigDecimal totalTravelExpenses = getCalculationOfTravelExpenses(travelRequestDto.getDAILY_ALLOWANCE(),
//                travelRequestDto.getStartDateTime(), travelRequestDto.getEndDateTime());
//
//        BigDecimal totalFoodExpenses = calculationOfFoodExpenses(travelRequestDto.getBreakfastQuantity(), travelRequestDto.getLunchQuantity(),
//                travelRequestDto.getDinnerQuantity(), travelRequestDto.getDAILY_ALLOWANCE());
//
//        return totalTravelExpenses.subtract(totalFoodExpenses);
//    }

//    private Long getHoursInTrip(LocalDateTime startDay, LocalDateTime endDay) {
//        Duration duration = Duration.between(startDay, endDay);
//        return duration.toHours();
//    }

//    private BigDecimal getCalculationOfTravelExpenses(BigDecimal dailyAllowance, LocalDateTime startDay, LocalDateTime endDay) {
//        long hoursInTravel = getHoursInTrip(startDay, endDay);
//        BigDecimal fiftyPercentOfDailyAllowance = BigDecimal.valueOf(50 / 100.0).multiply(dailyAllowance);
//
//        if (hoursInTravel <= 24) {
//            if (hoursInTravel < 8) {
//                return new BigDecimal(0);
//            }
//            if (hoursInTravel < 12) {
//                return fiftyPercentOfDailyAllowance;
//            }
//            return dailyAllowance;
//        } else {
//            long hoursInTravelLessThanDay = hoursInTravel % 24;
//            long fullDays = Duration.between(startDay, endDay).toDays(); //(hoursInTravel - hoursInTravelLessThanDay) / 24;
//            BigDecimal totalAmountForFullDays = BigDecimal.valueOf(fullDays).multiply(dailyAllowance);
//
//            if (hoursInTravelLessThanDay < 8) {
//                return totalAmountForFullDays.add(fiftyPercentOfDailyAllowance);
//            }
//            return totalAmountForFullDays.add(dailyAllowance);
//        }
//    }

//    private BigDecimal calculationOfFoodExpenses(Integer breakfastQuantity, Integer lunchQuantity, Integer dinnerQuantity, BigDecimal dailyAllowance) {
//        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
//        BigDecimal fiftyPercentOfDailyAllowance = BigDecimal.valueOf(50 / 100.0).multiply(dailyAllowance);
//        BigDecimal twentyFivePercentOfDailyAllowance = BigDecimal.valueOf(25 / 100.0).multiply(dailyAllowance);
//
//        BigDecimal moneyCostForBreakfast = BigDecimal.valueOf(breakfastQuantity).multiply(twentyFivePercentOfDailyAllowance);
//        BigDecimal moneyCostForLunch = BigDecimal.valueOf(lunchQuantity).multiply(fiftyPercentOfDailyAllowance);
//        BigDecimal moneyCostForDinner = BigDecimal.valueOf(dinnerQuantity).multiply(twentyFivePercentOfDailyAllowance);
//
//        return totalFoodExpenses.add(moneyCostForBreakfast).add(moneyCostForLunch).add(moneyCostForDinner);
//    }
}