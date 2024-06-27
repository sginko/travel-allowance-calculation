package pl.sginko.travelexpense.model.travel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.model.dietExpenses.service.DietExpensesService;
import pl.sginko.travelexpense.model.overnightStayExpenses.service.OvernightStayService;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.model.travel.repository.TravelRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TravelReadServiceImpl implements TravelReadService {
    //    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final DietExpensesService dietExpensesService;
    private  final OvernightStayService overnightStayService;

    public TravelReadServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper, DietExpensesService dietExpensesService, OvernightStayService overnightStayService) {
//        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
        this.dietExpensesService = dietExpensesService;
        this.overnightStayService = overnightStayService;
    }

    @Override
    public long getHoursInTravel(TravelRequestDto requestDto) {
        long hoursInTravel = Duration.between(requestDto.getStartTime().atDate(requestDto.getStartDate()),
                requestDto.getEndTime().atDate(requestDto.getEndDate())).toHours();
        return hoursInTravel;
    }

    @Override
    public int getTotalQuantityOfNight(TravelRequestDto requestDto) {
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
    public void updateTotalAmount(TravelRequestDto requestDto) {
//        this.totalAmount = (this.dietAmount.add(this.foodAmount).add(this.overnightStayAmount)).subtract(advancePayment);
        BigDecimal totalAmount = (overnightStayService.calculateOvernightStayAmount()
        dietAmount.add(this.foodAmount).add(this.overnightStayAmount)).subtract(advancePayment);
    }

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto) {
        TravelEntity entity = travelMapper.toEntity(requestDto);
//        entity.calculateDietAmount();
//        entity.calculateFoodAmount();
//        entity.calculateOvernightStayAmount();
//        travelRepository.save(entity);
        return travelMapper.toResponseDto(entity);
    }
}
