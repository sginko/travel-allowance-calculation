package pl.sginko.travelexpense.model.travel.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.model.dietExpenses.entity.DietExpensesEntity;
import pl.sginko.travelexpense.model.dietExpenses.service.DietExpensesService;
import pl.sginko.travelexpense.model.overnightStayExpenses.entity.OvernightStayExpensesEntity;
import pl.sginko.travelexpense.model.overnightStayExpenses.service.OvernightStayService;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TravelServiceImpl implements TravelService {
    private final TravelMapper travelMapper;
    private final DietExpensesService dietExpensesService;
    private final OvernightStayService overnightStayService;

    public TravelServiceImpl(TravelMapper travelMapper, @Lazy DietExpensesService dietExpensesService, OvernightStayService overnightStayService) {
        this.travelMapper = travelMapper;
        this.dietExpensesService = dietExpensesService;
        this.overnightStayService = overnightStayService;
    }

    @Override
    public Long getHoursInTravel(TravelRequestDto requestDto) {
        long hoursInTravel = Duration.between(requestDto.getStartTime().atDate(requestDto.getStartDate()),
                requestDto.getEndTime().atDate(requestDto.getEndDate())).toHours();
        return hoursInTravel;
    }

    @Override
    public Integer getTotalQuantityOfNight(TravelRequestDto requestDto) {
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

    @Override
    public BigDecimal updateTotalAmount(TravelRequestDto requestDto) {
//        this.totalAmount = (this.dietAmount.add(this.foodAmount).add(this.overnightStayAmount)).subtract(advancePayment);
        BigDecimal dietAmount = dietExpensesService.calculateDietAmount(requestDto);
        BigDecimal foodAmount = dietExpensesService.calculateFoodAmount(requestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStayAmount(requestDto);
        BigDecimal totalAmount = (dietAmount.add(foodAmount).add(overnightStayAmount)).subtract(requestDto.getAdvancePayment());
        return totalAmount;
    }

//    @Override
//    @Transactional
//    public TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto) {
//        TravelEntity entityFromTravelService = travelMapper.toEntityFromTravelService(requestDto);
//        DietExpensesEntity entityFromDietService = travelMapper.toEntityFromDietService(requestDto);
//        OvernightStayExpensesEntity entityFromOvernightStayService = travelMapper.toEntityFromOvernightStayService(requestDto);
//        dietExpensesService.calculateDietAmount(requestDto);
//        dietExpensesService.calculateFoodAmount(requestDto);
//        overnightStayService.calculateOvernightStayAmount(requestDto);
//        return travelMapper.toResponseDto(entityFromTravelService, entityFromDietService, entityFromOvernightStayService);
//    }

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto) {
        BigDecimal dietAmount = dietExpensesService.calculateDietAmount(requestDto);
        BigDecimal foodAmount = dietExpensesService.calculateFoodAmount(requestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStayAmount(requestDto);
        BigDecimal totalAmount = dietAmount.add(foodAmount).add(overnightStayAmount).subtract(requestDto.getAdvancePayment());

        TravelEntity entityFromTravelService = travelMapper.toEntityFromTravelService(requestDto);
        DietExpensesEntity entityFromDietService = travelMapper.toEntityFromDietService(requestDto);
        OvernightStayExpensesEntity entityFromOvernightStayService = travelMapper.toEntityFromOvernightStayService(requestDto);
        entityFromTravelService.setTotalAmount(totalAmount);
        entityFromDietService.setDietAmount(dietAmount);
        entityFromDietService.setFoodAmount(foodAmount);
        entityFromOvernightStayService.setOvernightStayAmount(overnightStayAmount);

        return travelMapper.toResponseDto(entityFromTravelService, entityFromDietService, entityFromOvernightStayService);
    }
}
