package pl.sginko.travelexpense.logic.travel.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sginko.travelexpense.logic.diet.model.dto.DietDto;
import pl.sginko.travelexpense.logic.overnightStay.exception.OvernightStayException;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.transport.exception.TransportException;
import pl.sginko.travelexpense.logic.transport.model.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.repository.TravelRepository;
import pl.sginko.travelexpense.logic.user.model.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.user.repository.UserRepository;
import pl.sginko.travelexpense.logic.user.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TravelServiceImplTest {
    private final Long PESEL = 90010101001L;
    private final String FIRST_NAME = "name";
    private final String SECOND_NAME = "surname";
    private final String POSITION = "position";

    private final String CITY_FROM = "cityFrom";
    private final String CITY_TO = "cityTo";
    private final LocalDate START_DAY = LocalDate.now();
    private final LocalTime START_TIME = LocalTime.of(0, 0);

    private final BigDecimal PERCENT_25 = BigDecimal.valueOf(0.25);
    private final BigDecimal PERCENT_50 = BigDecimal.valueOf(0.5);
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);
    private final BigDecimal HALF_DAILY_ALLOWANCE = DAILY_ALLOWANCE.multiply(PERCENT_50);
    private final BigDecimal ZERO_DAILY_ALLOWANCE = BigDecimal.ZERO;
    private final BigDecimal ONE_NIGHT_WITHOUT_INVOICE = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(1.5));
    private final BigDecimal ONE_NIGHT_WITH_INVOICE = BigDecimal.valueOf(100);
    private final BigDecimal MAX_AMOUNT_FOR_ONE_NIGHT_WITH_INVOICE = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(20));
    private final BigDecimal ADVANCE_PAYMENT = BigDecimal.valueOf(50);
    private final Boolean IS_INVOICE_AMOUNT_GREATER_ALLOWED = false;
    private final BigDecimal OTHER_EXPENSES = BigDecimal.valueOf(0);

    private final Integer INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST = 0;
    private final BigDecimal DOCUMENTED_LOCAL_TRANSPORT_COST = BigDecimal.valueOf(0);
    private final BigDecimal ONE_DAY_UNDOCUMENTED_LOCAL_TRANSPORT_COST = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(0.2));
    private final String MEANS_OF_TRANSPORT = "without transport";
    private final BigDecimal COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT = BigDecimal.valueOf(0);
    private final Long KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC = 0L;
    private final Long KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC = 0L;
    private final Long KILOMETERS_BY_MOTORCYCLE = 0L;
    private final Long KILOMETERS_BY_MOPED = 0L;

    private final BigDecimal COST_BY_CAR_ENGINE_UP_TO_900_CC = BigDecimal.valueOf(0.89);
    private final BigDecimal COST_BY_CAR_ENGINE_ABOVE_TO_900_CC = BigDecimal.valueOf(1.15);
    private final BigDecimal COST_BY_MOTORCYCLE = BigDecimal.valueOf(0.69);
    private final BigDecimal COST_BY_MOPED = BigDecimal.valueOf(0.42);

    @Autowired
    private TravelService travelService;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        travelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_calculate_expenses_for_short_trip_less_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(ZERO_DAILY_ALLOWANCE.add(OTHER_EXPENSES).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_8_and_less_12_hour() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(11, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(HALF_DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_long_trip_more_than_12_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(23, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_long_trip_more_than_12_hours_with_food() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(23, 59);

        Integer numberOfBreakfasts = 1;
        Integer numberOfLunches = 1;
        Integer numberOfDinners = 1;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(ZERO_DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_48_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(9, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_30_minutes() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(0, 30);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_less_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT))
                .add(HALF_DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_more_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(8, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT))
                .add(DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours_with_food() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 1;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 1;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_more_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_more_than_8_hours_with_food() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 1;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours_with_one_night_without_invoice() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 1;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT))
                .add(ONE_NIGHT_WITHOUT_INVOICE));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_more_than_8_hours_with_one_night_with_invoice() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(8, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 1;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE)
                .add(DAILY_ALLOWANCE).add(ONE_NIGHT_WITH_INVOICE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_more_than_8_hours_with_one_night_with_invoice_and_one_night_without_invoice_and_with_advancePayment() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(9, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 1;
        Integer inputQuantityOfOvernightStayWithInvoice = 1;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo((DAILY_ALLOWANCE.add(DAILY_ALLOWANCE)
                .add(DAILY_ALLOWANCE).add(ONE_NIGHT_WITH_INVOICE).add(ONE_NIGHT_WITHOUT_INVOICE)).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_overnight_stay_exception_for_trip_3_nights_with_input_two_nights_with_invoice_and_two_nights_without_invoice_and_with_advancePayment() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(3);
        LocalTime endTime = LocalTime.of(0, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 2;
        Integer inputQuantityOfOvernightStayWithInvoice = 2;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE.add(ONE_NIGHT_WITH_INVOICE);

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        Executable e = () -> travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    @DisplayName("Should throw OvernightStayException for invoice amount exceeding limit with one night stay")
    void testOvernightStayExceptionForExceededInvoiceAmount() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(18, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(18, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 1;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE.add(MAX_AMOUNT_FOR_ONE_NIGHT_WITH_INVOICE);

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        Executable e = () -> travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    void should_calculate_expenses_for_trip_24_hour_with_one_day_undocumented_cost() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        Integer inputtedDaysNumberForUndocumentedTransportCost = 1;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(inputtedDaysNumberForUndocumentedTransportCost, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(ONE_DAY_UNDOCUMENTED_LOCAL_TRANSPORT_COST)
                .subtract(ADVANCE_PAYMENT));
    }

    @Test
    void Should_throw_TransportException_for_trip_24_hour_with_two_day_undocumented_cost() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        Integer inputtedDaysNumberForUndocumentedTransportCost = 2;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(inputtedDaysNumberForUndocumentedTransportCost, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        Executable e = () -> travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThrows(TransportException.class, e);
    }

    @Test
    void should_calculate_expenses_for_trip_24_hour_with_one_day_documented_cost() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        BigDecimal documentedLocalTransportCost = BigDecimal.valueOf(50);

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, documentedLocalTransportCost,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(documentedLocalTransportCost).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_24_hour_with_travel_by_public_transport() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        BigDecimal costOfTravelByPublicTransport = BigDecimal.valueOf(100);

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, costOfTravelByPublicTransport, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(costOfTravelByPublicTransport).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_24_hour_with_travel_by_own_motorcycle() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        Long kilometersByMotorcycle = 100L;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                kilometersByMotorcycle, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo((DAILY_ALLOWANCE
                .add(COST_BY_MOTORCYCLE.multiply(BigDecimal.valueOf(kilometersByMotorcycle))))
                .subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_24_hour_with_travel_by_own_moped() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        Long kilometersByMoped = 100L;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, kilometersByMoped);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo((DAILY_ALLOWANCE
                .add(COST_BY_MOPED.multiply(BigDecimal.valueOf(kilometersByMoped))))
                .subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_24_hour_with_travel_by_own_car_engine_up_to_900_cc() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        Long kilometersByCarEngineUpTo900cc = 100L;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, kilometersByCarEngineUpTo900cc, KILOMETERS_BY_CAR_ENGINE_ABOVE_TO_900_CC,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo((DAILY_ALLOWANCE
                .add(COST_BY_CAR_ENGINE_UP_TO_900_CC.multiply(BigDecimal.valueOf(kilometersByCarEngineUpTo900cc))))
                .subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_24_hour_with_travel_by_own_car_engine_above_to_900_cc() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        Long kilometersByCarEngineAboveTo900cc = 100L;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(INPUTTED_DAYS_NUMBER_FOR_UNDOCUMENTED_LOCAL_TRANSPORT_COST, DOCUMENTED_LOCAL_TRANSPORT_COST,
                MEANS_OF_TRANSPORT, COST_OF_TRAVEL_BY_PUBLIC_TRANSPORT, KILOMETERS_BY_CAR_ENGINE_UP_TO_900_CC, kilometersByCarEngineAboveTo900cc,
                KILOMETERS_BY_MOTORCYCLE, KILOMETERS_BY_MOPED);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo((DAILY_ALLOWANCE
                .add(COST_BY_CAR_ENGINE_ABOVE_TO_900_CC.multiply(BigDecimal.valueOf(kilometersByCarEngineAboveTo900cc))))
                .subtract(ADVANCE_PAYMENT));
    }
}