package pl.sginko.travelexpense.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sginko.travelexpense.model.Dto.TravelRequestDto;
import pl.sginko.travelexpense.model.Dto.TravelResponseDto;
import pl.sginko.travelexpense.model.repository.TravelRepository;
import pl.sginko.travelexpense.model.service.TravelService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class TravelServiceImplTest {
    private final BigDecimal PERCENT_25 = new BigDecimal(0.25);
    private final BigDecimal PERCENT_50 = new BigDecimal(0.5);
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);
    private final BigDecimal HALF_DAILY_ALLOWANCE = DAILY_ALLOWANCE.multiply(PERCENT_50);
    private final BigDecimal ZERO_DAILY_ALLOWANCE = BigDecimal.ZERO;

    @Autowired
    private TravelService travelService;

    @Autowired
    private TravelRepository travelRepository;

    @AfterEach
    void tearDown() {
        travelRepository.deleteAll();
    }

    @Test
    void should_calculate_expenses_for_short_trip_less_than_8_hours_without_food() {
        //given
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0,0);
        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(7,59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, startTime, endDay, endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(ZERO_DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_trip_more_8_and_less_12_hour() {
        //given
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(11, 59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, startTime, endDay, endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(HALF_DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_long_trip_more_than_12_hours() {
        //given
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(23, 59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, startTime, endDay, endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_long_trip_more_than_12_hours_with_food() {
        //given
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(23, 59);
        Integer numberOfBreakfasts = 1;
        Integer numberOfLunches = 1;
        Integer numberOfDinners = 1;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, startTime, endDay, endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(ZERO_DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours() {
        //given
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, startTime, endDay, endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours_with_food() {
        //given
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);
        Integer numberOfBreakfasts = 1;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 1;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, startTime, endDay, endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_more_than_8_hours() {
        //given
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 0);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, startTime, endDay, endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_more_than_8_hours_with_food() {
        //given
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 0);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 1;
        Integer numberOfDinners = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, startTime, endDay, endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE));
    }
}