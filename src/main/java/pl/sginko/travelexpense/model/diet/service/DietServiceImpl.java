package pl.sginko.travelexpense.model.diet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.model.diet.dto.DietDto;
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
    public DietResponseDto calculateDiet(TravelRequestDto travelRequestDto) {
        DietDto dietDto = travelRequestDto.getDietDto();

        DietEntity entity = dietMapper.toEntity(dietDto, travelMapper.toEntity(travelRequestDto));

        calculateDietAmount(travelRequestDto, dietEntity);
        calculateFoodAmount(travelRequestDto, dietEntity);
        dietRepository.save(entity);
        return dietMapper.toResponseDto(entity);
    }

    private BigDecimal calculateDietAmount(TravelRequestDto travelRequestDto) {
        DietDto dietDto = travelRequestDto.getDietDto();
        LocalDate startDate = travelRequestDto.getStartDate();
        LocalTime startTime = travelRequestDto.getStartTime();
        LocalDate endDate = travelRequestDto.getEndDate();
        LocalTime endTime = travelRequestDto.getEndTime();
        long hoursInTravel = Duration.between(startTime.atDate(startDate), endTime.atDate(endDate)).toHours();
        BigDecimal fiftyPercentOfDailyAllowance = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.50));

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                dietEntity.setDietAmount(BigDecimal.ZERO);
            } else if (hoursInTravel < 12) {
                dietEntity.setDietAmount(fiftyPercentOfDailyAllowance);
            } else {
                dietEntity.setDietAmount(dietDto.getDailyAllowance());
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            BigDecimal totalAmountForFullDays = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours == 0) {
                dietEntity.setDietAmount(totalAmountForFullDays.add(BigDecimal.ZERO));
            } else if (remainingHours < 8) {
                dietEntity.setDietAmount(totalAmountForFullDays.add(fiftyPercentOfDailyAllowance));
            } else {
                dietEntity.setDietAmount(totalAmountForFullDays.add(dietDto.getDailyAllowance()));
            }
        }
        return d
    }

    private void calculateFoodAmount(TravelRequestDto travelRequestDto, DietEntity dietEntity) {
        DietDto dietDto = travelRequestDto.getDietDto();
        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = dietDto.getDailyAllowance().multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(dietDto.getNumberOfBreakfasts()).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(dietDto.getNumberOfLunches()).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(dietDto.getNumberOfDinners()).multiply(twentyFivePercentOfDailyAllowance);

        dietEntity.setFoodAmount(totalFoodExpenses.add(breakfastCost).add(lunchCost).add(dinnerCost).negate());
    }
}
