package pl.sginko.travelallowance.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;
import pl.sginko.travelallowance.repository.TravelRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class TravelServiceImplTest {
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);

    @Autowired
    private TravelService travelService;

    @Autowired
    private TravelRepository travelRepository;

    @AfterEach
    void tearDown() {
        travelRepository.deleteAll();
    }

    @Test
    void should_calculate_travel_expenses_if_travel_was_less_8_hour() {
        //given
        LocalDateTime startDay = LocalDateTime.now();
        LocalDateTime endDay = LocalDateTime.now().plusHours(7).plusMinutes(59);
        Integer breakfastQuantity = 0;
        Integer lunchQuantity = 0;
        Integer dinnerQuantity = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, endDay, breakfastQuantity, lunchQuantity, dinnerQuantity);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getCostOfTotalExpense().doubleValue()).isEqualTo(0.0);
    }

    @Test
    void should_calculate_travel_expenses_if_travel_was_more_8_and_less_12_hour() {
        //given
        LocalDateTime startDay = LocalDateTime.now();
        LocalDateTime endDay = LocalDateTime.now().plusHours(8).plusMinutes(30);
        Integer breakfastQuantity = 0;
        Integer lunchQuantity = 0;
        Integer dinnerQuantity = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, endDay, breakfastQuantity, lunchQuantity, dinnerQuantity);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getCostOfTotalExpense()).isEqualTo(DAILY_ALLOWANCE.multiply(new BigDecimal(0.50)).setScale(2));
    }

    @Test
    void should_calculate_travel_expenses_if_travel_was_more_12_hour() {
        //given
        LocalDateTime startDay = LocalDateTime.now();
        LocalDateTime endDay = LocalDateTime.now().plusHours(12).plusMinutes(30);
        Integer breakfastQuantity = 0;
        Integer lunchQuantity = 0;
        Integer dinnerQuantity = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, endDay, breakfastQuantity, lunchQuantity, dinnerQuantity);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getCostOfTotalExpense()).isEqualTo(DAILY_ALLOWANCE.setScale(2));
    }

    @Test
    void should_calculate_travel_expenses_if_travel_was_more_day_and_less_then_day_and_8_hour() {
        //given
        LocalDateTime startDay = LocalDateTime.now();
        LocalDateTime endDay = LocalDateTime.now().plusHours(27).plusMinutes(00);
        Integer breakfastQuantity = 0;
        Integer lunchQuantity = 0;
        Integer dinnerQuantity = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, endDay, breakfastQuantity, lunchQuantity, dinnerQuantity);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getCostOfTotalExpense()).isEqualTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE.multiply(new BigDecimal(0.50))).setScale(2));
    }

    @Test
    void should_calculate_travel_expenses_if_travel_was_more_32_hour() {
        //given
        LocalDateTime startDay = LocalDateTime.now();
        LocalDateTime endDay = LocalDateTime.now().plusHours(32).plusMinutes(00);
        Integer breakfastQuantity = 0;
        Integer lunchQuantity = 0;
        Integer dinnerQuantity = 0;
        TravelRequestDto travelRequestDto = new TravelRequestDto(startDay, endDay, breakfastQuantity, lunchQuantity, dinnerQuantity);

        //when
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //then
        assertThat(travelResponseDto.getCostOfTotalExpense()).isEqualTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE).setScale(2));
    }
}