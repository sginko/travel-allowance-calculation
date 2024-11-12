package pl.sginko.travelexpense.logic.travelexpense.overnightStay.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OvernightStayServiceTest {
    @Mock
    private OvernightStayMapper overnightStayMapper;

    @Mock
    private OvernightStayDto overnightStayDto;

    @InjectMocks
    private OvernightStayServiceImpl overnightStayService;

    private TravelEntity travelEntity;

    private OvernightStayEntity overnightStayEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(20, 0),
                LocalDate.now().plusDays(1), LocalTime.of(6, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelEntity.updateDietEntity(dietEntity);

        overnightStayEntity = new OvernightStayEntity(travelEntity,
                1, 0,
                BigDecimal.valueOf(500), false);

        when(overnightStayMapper.toEntity(overnightStayDto, travelEntity)).thenReturn(overnightStayEntity);
    }

    @Test
    void should_create_overnightStay_entity() {
        // WHEN
        OvernightStayEntity result = overnightStayService.createOvernightStayEntity(overnightStayDto, travelEntity);

        // THEN
        assertThat(result).isEqualTo(overnightStayEntity);
        verify(overnightStayMapper, times(1)).toEntity(overnightStayDto, travelEntity);
    }
}
