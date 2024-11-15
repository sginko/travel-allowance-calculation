package pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayEditDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class OvernightStayMapperTest {
    private OvernightStayMapper overnightStayMapper;
    private TravelReportEntity travelReportEntity;

    @BeforeEach
    void setUp() {
        overnightStayMapper = new OvernightStayMapper();
        travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(20, 0),
                LocalDate.now().plusDays(1), LocalTime.of(6, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45),
                0, 0, 0);
        travelReportEntity.setDietDetails(dietEntity);
    }

    @Test
    void should_map_OvernightStayDto_to_OvernightStayEntity_correctly() {
        // GIVEN
        OvernightStayDto overnightStayDto = new OvernightStayDto(1, 0,
                BigDecimal.valueOf(500), true);

        // WHEN
        OvernightStayEntity overnightStayEntity = overnightStayMapper.toEntity(overnightStayDto, travelReportEntity);

        // THEN
        assertThat(overnightStayEntity).isNotNull();
        assertThat(overnightStayEntity.getInputQuantityOfOvernightStayWithoutInvoice()).isEqualTo(1);
        assertThat(overnightStayEntity.getInputQuantityOfOvernightStayWithInvoice()).isEqualTo(0);
        assertThat(overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice()).isEqualByComparingTo(BigDecimal.valueOf(500));
        assertThat(overnightStayEntity.getIsInvoiceAmountGreaterAllowed()).isTrue();
        assertThat(overnightStayEntity.getTravelReportEntity()).isEqualTo(travelReportEntity);
    }

    @Test
    void should_map_OvernightStayEntity_to_OvernightStayResponseDto_correctly() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity, 1,
                0, BigDecimal.valueOf(500), true);

        // WHEN
        OvernightStayResponseDto overnightStayResponseDto = overnightStayMapper.toResponseDto(overnightStayEntity);

        // THEN
        assertThat(overnightStayResponseDto).isNotNull();
        assertThat(overnightStayResponseDto.getId()).isEqualTo(overnightStayEntity.getId());
        assertThat(overnightStayResponseDto.getInputQuantityOfOvernightStayWithoutInvoice())
                .isEqualTo(overnightStayEntity.getInputQuantityOfOvernightStayWithoutInvoice());
        assertThat(overnightStayResponseDto.getInputQuantityOfOvernightStayWithInvoice())
                .isEqualTo(overnightStayEntity.getInputQuantityOfOvernightStayWithInvoice());
        assertThat(overnightStayResponseDto.getTotalAmountOfOvernightsStayWithInvoice())
                .isEqualByComparingTo(overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice());
        assertThat(overnightStayResponseDto.getTotalAmountOfOvernightsStayWithoutInvoice())
                .isEqualByComparingTo(overnightStayEntity.getTotalAmountOfOvernightsStayWithoutInvoice());
        assertThat(overnightStayResponseDto.getOvernightStayAmount())
                .isEqualByComparingTo(overnightStayEntity.getOvernightStayAmount());
    }

    @Test
    void should_map_OvernightStayEntity_to_OvernightStayEditDto_correctly() {
        // GIVEN
        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity, 1,
                0, BigDecimal.valueOf(500), true);

        // WHEN
        OvernightStayEditDto overnightStayEditDto = overnightStayMapper.toOvernightStayEditDto(overnightStayEntity);

        // THEN
        assertThat(overnightStayEditDto).isNotNull();
        assertThat(overnightStayEditDto.getInputQuantityOfOvernightStayWithoutInvoice())
                .isEqualTo(overnightStayEntity.getInputQuantityOfOvernightStayWithoutInvoice());
        assertThat(overnightStayEditDto.getInputQuantityOfOvernightStayWithInvoice())
                .isEqualTo(overnightStayEntity.getInputQuantityOfOvernightStayWithInvoice());
        assertThat(overnightStayEditDto.getTotalAmountOfOvernightsStayWithInvoice())
                .isEqualByComparingTo(overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice());
        assertThat(overnightStayEditDto.getIsInvoiceAmountGreaterAllowed())
                .isEqualTo(overnightStayEntity.getIsInvoiceAmountGreaterAllowed());
    }
}
