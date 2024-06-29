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
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;

    public TravelServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
    }

    private void calculateDietAmount(TravelEntity entity) {
        long hoursInTravel = Duration.between(entity.getStartTime().atDate(entity.getStartDate()), entity.getEndTime().atDate(entity.getEndDate())).toHours();
        BigDecimal fiftyPercentOfDailyAllowance = entity.getDailyAllowance().multiply(BigDecimal.valueOf(0.50));

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                entity.setDietAmount(BigDecimal.ZERO);
            } else if (hoursInTravel < 12) {
                entity.setDietAmount(fiftyPercentOfDailyAllowance);
            } else {
                entity.setDietAmount(entity.getDailyAllowance());
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            BigDecimal totalAmountForFullDays = entity.getDailyAllowance().multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours == 0) {
                entity.setDietAmount(totalAmountForFullDays.add(BigDecimal.ZERO));
            } else if (remainingHours < 8) {
                entity.setDietAmount(totalAmountForFullDays.add(fiftyPercentOfDailyAllowance));
            } else {
                entity.setDietAmount(totalAmountForFullDays.add(entity.getDailyAllowance()));
            }
        }
        entity.updateTotalAmount();
    }

    private void calculateFoodAmount(TravelEntity entity) {
        BigDecimal fiftyPercentOfDailyAllowance = entity.getDailyAllowance().multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = entity.getDailyAllowance().multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(entity.getNumberOfBreakfasts()).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(entity.getNumberOfLunches()).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(entity.getNumberOfDinners()).multiply(twentyFivePercentOfDailyAllowance);

        entity.setFoodAmount(breakfastCost.add(lunchCost).add(dinnerCost).negate());
        entity.updateTotalAmount();
    }

    private void calculateOvernightStayAmount(TravelEntity entity) {
        int quantityOfOvernightStay = getTotalQuantityOfNight(entity);

        BigDecimal oneNightWithInvoice = entity.getDailyAllowance().multiply(BigDecimal.valueOf(20));
        BigDecimal oneNightWithoutInvoice = entity.getDailyAllowance().multiply(BigDecimal.valueOf(1.5));

        if (entity.getInputQuantityOfOvernightStayWithoutInvoice() > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        } else {
            entity.setAmountOfTotalOvernightsStayWithoutInvoice(oneNightWithoutInvoice.multiply(BigDecimal.valueOf(entity.getInputQuantityOfOvernightStayWithoutInvoice())));
        }

        if (entity.getInputQuantityOfOvernightStayWithInvoice() > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        }

        if ((entity.getInputQuantityOfOvernightStayWithInvoice() + entity.getInputQuantityOfOvernightStayWithoutInvoice()) > quantityOfOvernightStay) {
            throw new TravelException("Total input numbers of overnight stay more than total overnight stay");
        }
        entity.setTotalInputQuantityOfOvernightStay(entity.getInputQuantityOfOvernightStayWithInvoice() + entity.getInputQuantityOfOvernightStayWithoutInvoice());
        entity.setOvernightStayAmount(entity.getAmountOfTotalOvernightsStayWithoutInvoice().add(entity.getAmountOfTotalOvernightsStayWithInvoice()));

        entity.updateTotalAmount();
    }

    private int getTotalQuantityOfNight(TravelEntity entity) {
        int night = 0;
        LocalDateTime startDateTime = LocalDateTime.of(entity.getStartDate(), entity.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(entity.getEndDate(), entity.getEndTime());

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

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto) {
        TravelEntity entity = travelMapper.toEntity(requestDto);
        calculateDietAmount(entity);
        calculateFoodAmount(entity);
        calculateOvernightStayAmount(entity);
        travelRepository.save(entity);
        return travelMapper.toResponseDto(entity);
    }
}
