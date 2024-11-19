package pl.sginko.travelexpense.domain.travelReport.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayDto;
import pl.sginko.travelexpense.domain.travelReport.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domain.travelReport.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.service.overnightStay.OvernightStayServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OvernightStayServiceTest {
    @Mock
    private OvernightStayMapper overnightStayMapper;

    @Mock
    private OvernightStayDto overnightStayDto;

    @InjectMocks
    private OvernightStayServiceImpl overnightStayService;

    @Test
    void should_create_overnightStay_entity() {
        //GIVEN
        TravelReportEntity travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(20, 0),
                LocalDate.now().plusDays(1), LocalTime.of(6, 0),
                null, BigDecimal.ZERO, BigDecimal.ZERO);

        DietEntity dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45),
                0, 0, 0);

        travelReportEntity.setDietDetails(dietEntity);

        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReportEntity,
                1, 0,
                BigDecimal.valueOf(500), false);

        when(overnightStayMapper.toEntity(overnightStayDto, travelReportEntity)).thenReturn(overnightStayEntity);

        // WHEN
        OvernightStayEntity result = overnightStayService.createOvernightStayEntity(overnightStayDto, travelReportEntity);

        // THEN
        assertThat(result).isEqualTo(overnightStayEntity);
        verify(overnightStayMapper, times(1)).toEntity(overnightStayDto, travelReportEntity);
    }
}
