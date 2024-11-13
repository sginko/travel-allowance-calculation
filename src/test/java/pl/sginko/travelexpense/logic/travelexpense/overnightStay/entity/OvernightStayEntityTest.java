package pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.exception.OvernightStayException;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OvernightStayEntityTest {
    private TravelReportEntity travelReportEntity;
    private DietEntity dietEntity;

    @BeforeEach
    void setUp() {
        travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(20, 0),
                LocalDate.now().plusDays(2), LocalTime.of(6, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45), 0, 0, 0);

        travelReportEntity.setDietDetails(dietEntity);
    }

    @Test
    void should_calculate_quantity_of_overnightStay_correctly() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                0, 0,
                BigDecimal.ZERO, false);

        // WHEN
        int quantityOfOvernightStay = overnightStayEntity.getQuantityOfOvernightStay();

        // THEN
        assertThat(quantityOfOvernightStay).isEqualTo(2);
    }

    @Test
    void should_calculate_amountOfOvernightStay_without_invoice() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                2, 0,
                BigDecimal.ZERO, false);

        // WHEN
        BigDecimal amountOfOvernightStayWithoutInvoice = overnightStayEntity.getTotalAmountOfOvernightsStayWithoutInvoice();

        // THEN
        BigDecimal expectedAmount = BigDecimal.valueOf(135);
        assertThat(amountOfOvernightStayWithoutInvoice).isEqualByComparingTo(expectedAmount);
    }

    @Test
    void should_calculate_amountOfOvernightStay_when_invoice_amount_exceeds_limit_with_permission() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                0, 1,
                BigDecimal.valueOf(901), true);

        // WHEN
        BigDecimal amountOfOvernightStayWithInvoice = overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice();

        // THEN
        assertThat(amountOfOvernightStayWithInvoice).isEqualByComparingTo(BigDecimal.valueOf(901));
    }

    @Test
    void should_throw_exception_when_invoice_amount_exceeds_limit_without_permission() {
        // WHEN
        Executable e = () -> new OvernightStayEntity(travelReportEntity,
                0, 1,
                BigDecimal.valueOf(901), false);

        // THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    void should_calculate_totalOvernightStayAmount() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                1, 1,
                BigDecimal.valueOf(900), false);

        // WHEN
        BigDecimal totalOvernightStayAmount = overnightStayEntity.calculateOvernightStayAmount();

        // THEN
        BigDecimal expectedTotal = overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice()
                .add(overnightStayEntity.getTotalAmountOfOvernightsStayWithoutInvoice());

        assertThat(totalOvernightStayAmount).isEqualByComparingTo(expectedTotal);
    }

    @Test
    void should_not_calculate_amount_for_short_night_without_invoice() {
        // GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(23, 0),
                LocalDate.now().plusDays(1), LocalTime.of(4, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelReportEntity.setDietDetails(dietEntity);

        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                0, 0,
                BigDecimal.ZERO, false);

        // WHEN
        BigDecimal amountOfOvernightStayWithoutInvoice = overnightStayEntity.getTotalAmountOfOvernightsStayWithoutInvoice();

        // THEN
        assertThat(amountOfOvernightStayWithoutInvoice).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void should_throw_OvernightStayException_when_input_quantity_exceeds_calculated_quantity_of_overnight_stay() {
        // WHEN
        Executable e = () -> new OvernightStayEntity(travelReportEntity,
                2, 1,
                BigDecimal.ZERO, false);

        // THEN
        assertThrows(OvernightStayException.class, e);
    }
}
