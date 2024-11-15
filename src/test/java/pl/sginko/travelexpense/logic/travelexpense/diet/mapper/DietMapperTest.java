package pl.sginko.travelexpense.logic.travelexpense.diet.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietEditDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class DietMapperTest {
    private DietMapper dietMapper;
    private TravelReportEntity travelReportEntity;

    @BeforeEach
    void setUp() {
        dietMapper = new DietMapper();
        travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Test
    void should_map_DietDto_to_DietEntity_correctly() {
        // GIVEN
        DietDto dietDto = new DietDto(BigDecimal.valueOf(45), 1, 1, 1);

        // WHEN
        DietEntity dietEntity = dietMapper.toEntity(dietDto, travelReportEntity);

        // THEN
        assertThat(dietEntity).isNotNull();
        assertThat(dietEntity.getDailyAllowance()).isEqualByComparingTo(BigDecimal.valueOf(45));
        assertThat(dietEntity.getNumberOfBreakfasts()).isEqualTo(1);
        assertThat(dietEntity.getNumberOfLunches()).isEqualTo(1);
        assertThat(dietEntity.getNumberOfDinners()).isEqualTo(1);
        assertThat(dietEntity.getTravelReportEntity()).isEqualTo(travelReportEntity);
    }

    @Test
    void should_map_DietEntity_to_DietResponseDto_correctly() {
        // GIVEN
        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45), 1, 1, 1);

        // WHEN
        DietResponseDto dietResponseDto = dietMapper.toResponseDto(dietEntity);

        // THEN
        assertThat(dietResponseDto).isNotNull();
        assertThat(dietResponseDto.getId()).isEqualTo(dietEntity.getId());
        assertThat(dietResponseDto.getNumberOfBreakfasts()).isEqualTo(dietEntity.getNumberOfBreakfasts());
        assertThat(dietResponseDto.getNumberOfLunches()).isEqualTo(dietEntity.getNumberOfLunches());
        assertThat(dietResponseDto.getNumberOfDinners()).isEqualTo(dietEntity.getNumberOfDinners());
        assertThat(dietResponseDto.getFoodAmount()).isEqualByComparingTo(dietEntity.getFoodAmount());
        assertThat(dietResponseDto.getDietAmount()).isEqualByComparingTo(dietEntity.getDietAmount());
    }

    @Test
    void should_map_DietEntity_to_DietEditDto_correctly() {
        // GIVEN
        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45), 2,
                3, 1);

        // WHEN
        DietEditDto dietEditDto = dietMapper.toDietEditDto(dietEntity);

        // THEN
        assertThat(dietEditDto).isNotNull();
        assertThat(dietEditDto.getDailyAllowance()).isEqualByComparingTo(dietEntity.getDailyAllowance());
        assertThat(dietEditDto.getNumberOfBreakfasts()).isEqualTo(dietEntity.getNumberOfBreakfasts());
        assertThat(dietEditDto.getNumberOfLunches()).isEqualTo(dietEntity.getNumberOfLunches());
        assertThat(dietEditDto.getNumberOfDinners()).isEqualTo(dietEntity.getNumberOfDinners());
    }
}
