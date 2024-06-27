package pl.sginko.travelexpense.travel.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sginko.travelexpense.model.employee.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.model.employee.repository.EmployeeRepository;
import pl.sginko.travelexpense.model.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.model.employee.service.EmployeeService;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.repository.TravelRepository;
import pl.sginko.travelexpense.model.travel.service.TravelReadService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class TravelReadServiceImplTest {
    private final BigDecimal PERCENT_25 = new BigDecimal(0.25);
    private final BigDecimal PERCENT_50 = new BigDecimal(0.5);
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);
    private final BigDecimal HALF_DAILY_ALLOWANCE = DAILY_ALLOWANCE.multiply(PERCENT_50);
    private final BigDecimal ZERO_DAILY_ALLOWANCE = BigDecimal.ZERO;
    private final BigDecimal ONE_NIGHT_WITHOUT_INVOICE = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(1.5));
    private final BigDecimal ONE_NIGHT_WITH_INVOICE = BigDecimal.valueOf(100);
    private final BigDecimal ADVANCE_PAYMENT = BigDecimal.valueOf(50);


    @Autowired
    private TravelReadService travelReadService;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeReaderService employeeReaderService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void tearDown() {
        travelRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void should_calculate_expenses_for_short_trip_less_than_8_hours() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(7, 59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(ZERO_DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_trip_more_8_and_less_12_hour() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(11, 59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(HALF_DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_long_trip_more_than_12_hours() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(23, 59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_long_trip_more_than_12_hours_with_food() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(23, 59);
        Integer numberOfBreakfasts = 1;
        Integer numberOfLunches = 1;
        Integer numberOfDinners = 1;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(ZERO_DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_48_hours() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(9, 0);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_30_minutes() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(0, 30);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_less_than_8_hours() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(7, 59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE)
                .add(HALF_DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_more_than_8_hours() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(8, 0);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE)
                .add(DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours_with_food() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);
        Integer numberOfBreakfasts = 1;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 1;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE);
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_more_than_8_hours() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 0);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_more_than_8_hours_with_food() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 0);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 1;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours_with_one_night_without_invoice() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 1;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE)
                .add(ONE_NIGHT_WITHOUT_INVOICE));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_more_than_8_hours_with_one_night_with_invoice() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(8, 0);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 1;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE;
        BigDecimal advancePayment = BigDecimal.ZERO;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE)
                .add(DAILY_ALLOWANCE).add(ONE_NIGHT_WITH_INVOICE));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_more_than_8_hours_with_one_night_with_invoice_and_one_night_without_invoice_and_with_advancePayment() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        String cityFrom = "cityFrom";
        String cityTo = "cityTo";
        LocalDate startDay = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(9, 0);
        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 1;
        Integer inputQuantityOfOvernightStayWithInvoice = 1;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE;
        BigDecimal advancePayment = ADVANCE_PAYMENT;

        TravelRequestDto travelRequestDto = new TravelRequestDto(pesel, cityFrom, cityTo, startDay, startTime, endDay,
                endTime, numberOfBreakfasts, numberOfLunches, numberOfDinners, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice, advancePayment);

        //WHEN
        TravelResponseDto travelResponseDto = travelReadService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo((DAILY_ALLOWANCE.add(DAILY_ALLOWANCE)
                .add(DAILY_ALLOWANCE).add(ONE_NIGHT_WITH_INVOICE).add(ONE_NIGHT_WITHOUT_INVOICE)).subtract(ADVANCE_PAYMENT));
    }

    //TODO add Test with throw
}