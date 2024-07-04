package pl.sginko.travelexpense.model.diet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.model.diet.dto.DietRequestDto;
import pl.sginko.travelexpense.model.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.model.diet.entity.DietEntity;
import pl.sginko.travelexpense.model.diet.mapper.DietMapper;
import pl.sginko.travelexpense.model.diet.repository.DietRepository;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DietServiceImpl implements DietService {
    private final DietRepository dietRepository;
    private final DietMapper dietMapper;
    private final TravelMapper travelMapper;

    public DietServiceImpl(DietRepository dietRepository, DietMapper dietMapper, TravelMapper travelMapper) {
        this.dietRepository = dietRepository;
        this.dietMapper = dietMapper;
        this.travelMapper = travelMapper;
    }

    @Override
    @Transactional
    public DietResponseDto calculateDiet(TravelRequestDto travelRequestDto, DietRequestDto dietRequestDto, DietEntity dietEntity) {
        DietEntity entity = dietMapper.toEntity(dietRequestDto, travelMapper.toEntity(travelRequestDto));
        calculateDietAmount(travelRequestDto, dietRequestDto, dietEntity);
        calculateFoodAmount(dietRequestDto, dietEntity);
        dietRepository.save(entity);
        return dietMapper.toResponseDto(entity);
    }

    private void calculateDietAmount(TravelRequestDto travelRequestDto, DietRequestDto dietRequestDto, DietEntity dietEntity) {
        LocalDate startDate = travelRequestDto.getStartDate();
        LocalTime startTime = travelRequestDto.getStartTime();
        LocalDate endDate = travelRequestDto.getEndDate();
        LocalTime endTime = travelRequestDto.getEndTime();
        long hoursInTravel = Duration.between(startTime.atDate(startDate), endTime.atDate(endDate)).toHours();
        BigDecimal fiftyPercentOfDailyAllowance = dietRequestDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.50));

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                dietEntity.setDietAmount(BigDecimal.ZERO);
            } else if (hoursInTravel < 12) {
                dietEntity.setDietAmount(fiftyPercentOfDailyAllowance);
            } else {
                dietEntity.setDietAmount(dietRequestDto.getDailyAllowance());
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            BigDecimal totalAmountForFullDays = dietRequestDto.getDailyAllowance().multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours == 0) {
                dietEntity.setDietAmount(totalAmountForFullDays.add(BigDecimal.ZERO));
            } else if (remainingHours < 8) {
                dietEntity.setDietAmount(totalAmountForFullDays.add(fiftyPercentOfDailyAllowance));
            } else {
                dietEntity.setDietAmount(totalAmountForFullDays.add(dietRequestDto.getDailyAllowance()));
            }
        }
    }

    private void calculateFoodAmount(DietRequestDto dietRequestDto, DietEntity dietEntity) {
        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = dietRequestDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = dietRequestDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(dietRequestDto.getNumberOfBreakfasts()).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(dietRequestDto.getNumberOfLunches()).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(dietRequestDto.getNumberOfDinners()).multiply(twentyFivePercentOfDailyAllowance);

        dietEntity.setFoodAmount(totalFoodExpenses.add(breakfastCost).add(lunchCost).add(dinnerCost).negate());
    }
}
