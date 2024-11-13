package pl.sginko.travelexpense.logic.travelexpense.diet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DietServiceTest {
    @Mock
    private DietMapper dietMapper;

    @InjectMocks
    private DietServiceImpl dietService;

    @Mock
    private DietDto dietDto;

    private TravelEntity travelEntity;

    private DietEntity dietEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        travelEntity = new TravelEntity("CityA", "CityB", LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0), null, BigDecimal.ZERO, BigDecimal.ZERO);

        dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 1, 1, 1);

        when(dietMapper.toEntity(dietDto, travelEntity)).thenReturn(dietEntity);
    }

    @Test
    void should_create_DietEntity() {
        // WHEN
        DietEntity result = dietService.createDietEntity(dietDto, travelEntity);

        // THEN
        assertThat(result).isEqualTo(dietEntity);
        verify(dietMapper).toEntity(dietDto, travelEntity);
    }
}