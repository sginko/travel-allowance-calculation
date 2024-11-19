package pl.sginko.travelexpense.domain.travelReport.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayEditDto;
import pl.sginko.travelexpense.domain.travelReport.exception.OvernightStayException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OvernightStayEntityTest {
    private static final BigDecimal DAILY_ALLOWANCE = BigDecimal.valueOf(45);
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalTime START_TIME = LocalTime.of(20, 0);
    private static final LocalTime END_TIME = LocalTime.of(6, 0);

    private TravelReportEntity travelReportEntity;
    private DietEntity dietEntity;

    @BeforeEach
    void setUp() {
        travelReportEntity = new TravelReportEntity("CityA", "CityB", START_DATE, START_TIME,
                START_DATE.plusDays(2), END_TIME, null, BigDecimal.ZERO, BigDecimal.ZERO);

        dietEntity = new DietEntity(travelReportEntity, DAILY_ALLOWANCE, 0, 0,
                0);

        travelReportEntity.setDietDetails(dietEntity);
    }

    @Test
    void should_calculate_quantity_of_overnightStay_correctly() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                0, 0, BigDecimal.ZERO,
                false);

        // WHEN
        int quantityOfOvernightStay = overnightStayEntity.getQuantityOfOvernightStay();

        // THEN
        assertThat(quantityOfOvernightStay).isEqualTo(2);
    }

    @Test
    void should_calculate_amountOfOvernightStay_without_invoice() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                2, 0, BigDecimal.ZERO,
                false);

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
                0, 1, BigDecimal.valueOf(901),
                true);

        // WHEN
        BigDecimal amountOfOvernightStayWithInvoice = overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice();

        // THEN
        assertThat(amountOfOvernightStayWithInvoice).isEqualByComparingTo(BigDecimal.valueOf(901));
    }

    @Test
    void should_throw_exception_when_invoice_amount_exceeds_limit_without_permission() {
        // GIVEN
        Executable e = () -> new OvernightStayEntity(travelReportEntity, 0,
                1, BigDecimal.valueOf(901), false);

        // THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    void should_calculate_totalOvernightStayAmount() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                1, 1, BigDecimal.valueOf(900),
                false);

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
        TravelReportEntity shortNightReportEntity = new TravelReportEntity("CityA", "CityB",
                START_DATE, LocalTime.of(23, 0), START_DATE.plusDays(1), LocalTime.of(4, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity shortNightDietEntity = new DietEntity(shortNightReportEntity, DAILY_ALLOWANCE,
                0, 0, 0);

        shortNightReportEntity.setDietDetails(shortNightDietEntity);

        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(shortNightReportEntity,
                0, 0, BigDecimal.ZERO, false);

        // WHEN
        BigDecimal amountOfOvernightStayWithoutInvoice = overnightStayEntity.getTotalAmountOfOvernightsStayWithoutInvoice();

        // THEN
        assertThat(amountOfOvernightStayWithoutInvoice).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void should_throw_OvernightStayException_when_input_quantity_exceeds_calculated_quantity_of_overnight_stay() {
        // GIVEN
        Executable e = () -> new OvernightStayEntity(travelReportEntity, 2,
                1, BigDecimal.ZERO, false);

        // THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    void should_throw_OvernightStayException_when_inputQuantityOfOvernightStayWithInvoice_exceeds_calculatedQuantity() {
        // GIVEN
        Executable e = () -> new OvernightStayEntity(travelReportEntity, 0,
                3, BigDecimal.ZERO, false);

        // THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    void should_throw_OvernightStayException_when_inputQuantityOfOvernightStayWithoutInvoice_exceeds_calculatedQuantity() {
        // GIVEN
        Executable e = () -> new OvernightStayEntity(travelReportEntity, 3,
                0, BigDecimal.ZERO, false);

        // THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    void should_update_overnightStayDetails_based_on_overnightStayEditDto() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                1, 1, BigDecimal.valueOf(200),
                false);

        OvernightStayEditDto overnightStayEditDto = new OvernightStayEditDto(1,
                1, BigDecimal.valueOf(500), true);

        // WHEN
        overnightStayEntity.updateOvernightStayDetails(overnightStayEditDto);

        // THEN
        assertThat(overnightStayEntity.getInputQuantityOfOvernightStayWithoutInvoice())
                .isEqualTo(overnightStayEditDto.getInputQuantityOfOvernightStayWithoutInvoice());
        assertThat(overnightStayEntity.getInputQuantityOfOvernightStayWithInvoice())
                .isEqualTo(overnightStayEditDto.getInputQuantityOfOvernightStayWithInvoice());
        assertThat(overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice())
                .isEqualByComparingTo(overnightStayEditDto.getTotalAmountOfOvernightsStayWithInvoice());
        assertThat(overnightStayEntity.getIsInvoiceAmountGreaterAllowed())
                .isEqualTo(overnightStayEditDto.getIsInvoiceAmountGreaterAllowed());

        int expectedTotalInputQuantity = overnightStayEntity.getInputQuantityOfOvernightStayWithoutInvoice()
                + overnightStayEntity.getInputQuantityOfOvernightStayWithInvoice();

        BigDecimal expectedTotalAmountOfOvernightsStayWithoutInvoice = calculateExpectedTotalAmountOfOvernightStayWithoutInvoice(overnightStayEntity);
        BigDecimal expectedOvernightStayAmount = overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice()
                .add(expectedTotalAmountOfOvernightsStayWithoutInvoice);

        assertThat(overnightStayEntity.getTotalInputQuantityOfOvernightStay()).isEqualTo(expectedTotalInputQuantity);
        assertThat(overnightStayEntity.getTotalAmountOfOvernightsStayWithoutInvoice())
                .isEqualByComparingTo(expectedTotalAmountOfOvernightsStayWithoutInvoice);
        assertThat(overnightStayEntity.getOvernightStayAmount()).isEqualByComparingTo(expectedOvernightStayAmount);
    }

    private BigDecimal calculateExpectedTotalAmountOfOvernightStayWithoutInvoice(OvernightStayEntity overnightStayEntity) {
        BigDecimal dailyAllowance = overnightStayEntity.getTravelReportEntity().getDietEntity().getDailyAllowance();
        BigDecimal oneNightWithoutInvoice = dailyAllowance.multiply(BigDecimal.valueOf(1.5));
        return oneNightWithoutInvoice.multiply(BigDecimal.valueOf(overnightStayEntity.getInputQuantityOfOvernightStayWithoutInvoice()));
    }
}
