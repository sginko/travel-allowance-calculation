package pl.sginko.travelexpense.logic.overnightStay.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import pl.sginko.travelexpense.logic.diet.model.dto.DietDto;
import pl.sginko.travelexpense.logic.overnightStay.exception.OvernightStayException;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class OvernightStayServiceTest {
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);

    @Autowired
    private OvernightStayService overnightStayService;

    @MockBean
    private TravelRequestDto travelRequestDto;

    @MockBean
    private DietDto dietDto;

    @MockBean
    private OvernightStayDto overnightStayDto;

    @Test
    void should_calculate_total_input_quantity_of_overnight_stay() {
        //GIVEN
        when(travelRequestDto.getOvernightStayDto()).thenReturn(overnightStayDto);
        when(overnightStayDto.getInputQuantityOfOvernightStayWithInvoice()).thenReturn(1);
        when(overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice()).thenReturn(1);

        //WHEN
        Integer totalInputQuantityOfOvernightStay = overnightStayService.calculateTotalInputQuantityOfOvernightStay(travelRequestDto);

        //THEN
        assertThat(totalInputQuantityOfOvernightStay).isEqualByComparingTo(2);
    }

    @Test
    void should_calculate_quantity_of_overnight_stay_when_night_in_trip_less_than_6_hours() {
        //GIVEN
        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(02, 59));

        //WHEN
        Integer quantityOfOvernightStay = overnightStayService.calculateQuantityOfOvernightStay(travelRequestDto);

        //THEN
        assertThat(quantityOfOvernightStay).isEqualByComparingTo(0);
    }

    @Test
    void should_calculate_quantity_of_overnight_stay_when_night_in_trip_more_than_6_hours() {
        //GIVEN
        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(03, 00));

        //WHEN
        Integer quantityOfOvernightStay = overnightStayService.calculateQuantityOfOvernightStay(travelRequestDto);

        //THEN
        assertThat(quantityOfOvernightStay).isEqualByComparingTo(1);
    }

    @Test
    void should_calculate_amount_of_overnight_stay_with_invoice_for_one_night() {
        //GIVEN
        BigDecimal oneNightWithInvoice = BigDecimal.valueOf(100);

        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);

        when(travelRequestDto.getOvernightStayDto()).thenReturn(overnightStayDto);
        when(overnightStayDto.getAmountOfTotalOvernightsStayWithInvoice()).thenReturn(oneNightWithInvoice);

        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(03, 00));

        //WHEN
        BigDecimal amountOfOvernightStayWithInvoice = overnightStayService.calculateAmountOfOvernightStayWithInvoice(travelRequestDto);

        //THEN
        assertThat(amountOfOvernightStayWithInvoice).isEqualByComparingTo(oneNightWithInvoice);
    }

    @Test
    void should_calculate_amount_of_overnight_stay_without_invoice_for_one_night() {
        //GIVEN
        BigDecimal oneNightWithoutInvoice = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(1.5));

        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);

        when(travelRequestDto.getOvernightStayDto()).thenReturn(overnightStayDto);
        when(overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice()).thenReturn(1);

        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(03, 00));

        //WHEN
        BigDecimal amountOfOvernightStayWithoutInvoice = overnightStayService.calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);

        //THEN
        assertThat(amountOfOvernightStayWithoutInvoice).isEqualByComparingTo(oneNightWithoutInvoice);
    }

    @Test
    void calculateOvernightStay() {
        //GIVEN
        BigDecimal oneNightWithoutInvoice = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(1.5));
        BigDecimal oneNightWithInvoice = BigDecimal.valueOf(100);

        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);

        when(travelRequestDto.getOvernightStayDto()).thenReturn(overnightStayDto);
        when(overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice()).thenReturn(1);
        when(overnightStayDto.getAmountOfTotalOvernightsStayWithInvoice()).thenReturn(oneNightWithInvoice);

        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(03, 00));

        //WHEN
        BigDecimal overnightStay = overnightStayService.calculateOvernightStay(travelRequestDto);

        //THEN
        assertThat(overnightStay).isEqualByComparingTo(oneNightWithoutInvoice.add(oneNightWithInvoice));
    }

    @Test
    void should_throw_overnightStayException_for_invoice_amount_exceeding_limit_with_one_night_stay() {
        //GIVEN
        BigDecimal oneNightWithInvoice = BigDecimal.valueOf(1000);

        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
        when(dietDto.getDailyAllowance()).thenReturn(DAILY_ALLOWANCE);

        when(travelRequestDto.getOvernightStayDto()).thenReturn(overnightStayDto);
        when(overnightStayDto.getAmountOfTotalOvernightsStayWithInvoice()).thenReturn(oneNightWithInvoice);
        when(overnightStayDto.getIsInvoiceAmountGreaterAllowed()).thenReturn(false);

        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(03, 00));

        //WHEN
        Executable e = () -> overnightStayService.calculateAmountOfOvernightStayWithInvoice(travelRequestDto);

        //THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    void should_throw_overnightStayException_for_trip_2_nights_with_input_three_nights_without_invoice() {
        //GIVEN
        when(travelRequestDto.getOvernightStayDto()).thenReturn(overnightStayDto);
        when(overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice()).thenReturn(3);

        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(03, 00));

        //WHEN
        Executable e = () -> overnightStayService.calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);

        //THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    void should_throw_overnightStayException_for_trip_2_nights_with_input_three_nights_with_invoice() {
        //GIVEN
        when(travelRequestDto.getOvernightStayDto()).thenReturn(overnightStayDto);
        when(overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice()).thenReturn(3);

        when(travelRequestDto.getStartDate()).thenReturn(LocalDate.now());
        when(travelRequestDto.getStartTime()).thenReturn(LocalTime.of(0, 0));
        when(travelRequestDto.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        when(travelRequestDto.getEndTime()).thenReturn(LocalTime.of(03, 00));

        //WHEN
        Executable e = () -> overnightStayService.calculateAmountOfOvernightStayWithInvoice(travelRequestDto);

        //THEN
        assertThrows(OvernightStayException.class, e);
    }
}
