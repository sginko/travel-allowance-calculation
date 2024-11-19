package pl.sginko.travelexpense.domain.travelReport.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.domain.travelReport.mapper.TravelReportMapper;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietResponseDto;
import pl.sginko.travelexpense.domain.travelReport.mapper.DietMapper;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayResponseDto;
import pl.sginko.travelexpense.domain.travelReport.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostResponseDto;
import pl.sginko.travelexpense.domain.travelReport.mapper.TransportCostMapper;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportRequestDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TravelReportMapperTest {
    @InjectMocks
    private TravelReportMapper travelReportMapper;

    @Mock
    private DietMapper dietMapper;

    @Mock
    private OvernightStayMapper overnightStayMapper;

    @Mock
    private TransportCostMapper transportCostMapper;

    private TravelReportRequestDto travelReportRequestDto;
    private UserEntity userEntity;
    private TravelReportEntity travelReportEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity("user@example.com", "John", "Doe", "password123");

        travelReportRequestDto = new TravelReportRequestDto("CityA", "CityB", LocalDate.now(),
                LocalTime.of(8, 0), LocalDate.now().plusDays(2), LocalTime.of(18, 0),
                BigDecimal.valueOf(100), BigDecimal.valueOf(50), null, null, null);

        travelReportEntity = new TravelReportEntity("CityA", "CityB", LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0), userEntity, BigDecimal.valueOf(100),
                BigDecimal.valueOf(50));
    }

    @Test
    void should_map_TravelRequestDto_to_TravelEntity_correctly() {
        // WHEN
        TravelReportEntity mappedEntity = travelReportMapper.toTravelEntity(travelReportRequestDto, userEntity);

        // THEN
        assertThat(mappedEntity).isNotNull();
        assertThat(mappedEntity.getFromCity()).isEqualTo("CityA");
        assertThat(mappedEntity.getToCity()).isEqualTo("CityB");
        assertThat(mappedEntity.getStartDate()).isEqualTo(travelReportRequestDto.getStartDate());
        assertThat(mappedEntity.getStartTime()).isEqualTo(travelReportRequestDto.getStartTime());
        assertThat(mappedEntity.getEndDate()).isEqualTo(travelReportRequestDto.getEndDate());
        assertThat(mappedEntity.getEndTime()).isEqualTo(travelReportRequestDto.getEndTime());
        assertThat(mappedEntity.getAdvancePayment()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(mappedEntity.getOtherExpenses()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(mappedEntity.getUserEntity()).isEqualTo(userEntity);
    }

    @Test
    void should_map_TravelEntity_to_TravelResponseDto_correctly() {
        // GIVEN
        DietResponseDto dietResponseDto = mock(DietResponseDto.class);
        OvernightStayResponseDto overnightStayResponseDto = mock(OvernightStayResponseDto.class);
        TransportCostResponseDto transportCostResponseDto = mock(TransportCostResponseDto.class);

        when(dietMapper.toResponseDto(travelReportEntity.getDietEntity())).thenReturn(dietResponseDto);
        when(overnightStayMapper.toResponseDto(travelReportEntity.getOvernightStayEntity())).thenReturn(overnightStayResponseDto);
        when(transportCostMapper.toResponseDto(travelReportEntity.getTransportCostEntity())).thenReturn(transportCostResponseDto);

        // WHEN
        TravelReportResponseDto responseDto = travelReportMapper.toResponseDto(travelReportEntity);

        // THEN
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getFromCity()).isEqualTo(travelReportEntity.getFromCity());
        assertThat(responseDto.getToCity()).isEqualTo(travelReportEntity.getToCity());
        assertThat(responseDto.getEmail()).isEqualTo(travelReportEntity.getUserEntity().getEmail());
        assertThat(responseDto.getDietResponse()).isEqualTo(dietResponseDto);
        assertThat(responseDto.getOvernightStayResponseDto()).isEqualTo(overnightStayResponseDto);
        assertThat(responseDto.getTransportCostResponseDto()).isEqualTo(transportCostResponseDto);
    }

    @Test
    void should_map_TravelEntity_to_TravelSubmissionResponseDto_correctly() {
        // WHEN
        travelReportEntity.updateStatus(TravelReportStatus.APPROVED);
        TravelReportSubmissionResponseDto submissionResponseDto = travelReportMapper.toTravelSubmissionResponseDto(travelReportEntity);

        // THEN
        assertThat(submissionResponseDto).isNotNull();
        assertThat(submissionResponseDto.getStatus()).isEqualTo(TravelReportStatus.APPROVED);
    }

    @Test
    void should_map_TravelEntity_to_TravelEditDto_correctly() {
        // GIVEN
        DietEditDto dietEditDto = mock(DietEditDto.class);
        OvernightStayEditDto overnightStayEditDto = mock(OvernightStayEditDto.class);
        TransportCostEditDto transportCostEditDto = mock(TransportCostEditDto.class);

        when(dietMapper.toDietEditDto(travelReportEntity.getDietEntity())).thenReturn(dietEditDto);
        when(overnightStayMapper.toOvernightStayEditDto(travelReportEntity.getOvernightStayEntity())).thenReturn(overnightStayEditDto);
        when(transportCostMapper.toTransportCosEditDto(travelReportEntity.getTransportCostEntity())).thenReturn(transportCostEditDto);

        // WHEN
        TravelReportEditDto travelReportEditDto = travelReportMapper.toTravelEditDto(travelReportEntity);

        // THEN
        assertThat(travelReportEditDto).isNotNull();
        assertThat(travelReportEditDto.getFromCity()).isEqualTo(travelReportEntity.getFromCity());
        assertThat(travelReportEditDto.getToCity()).isEqualTo(travelReportEntity.getToCity());
        assertThat(travelReportEditDto.getStartDate()).isEqualTo(travelReportEntity.getStartDate());
        assertThat(travelReportEditDto.getStartTime()).isEqualTo(travelReportEntity.getStartTime());
        assertThat(travelReportEditDto.getEndDate()).isEqualTo(travelReportEntity.getEndDate());
        assertThat(travelReportEditDto.getEndTime()).isEqualTo(travelReportEntity.getEndTime());
        assertThat(travelReportEditDto.getAdvancePayment()).isEqualByComparingTo(travelReportEntity.getAdvancePayment());
        assertThat(travelReportEditDto.getOtherExpenses()).isEqualByComparingTo(travelReportEntity.getOtherExpenses());
        assertThat(travelReportEditDto.getDietEditDto()).isEqualTo(dietEditDto);
        assertThat(travelReportEditDto.getOvernightStayEditDto()).isEqualTo(overnightStayEditDto);
        assertThat(travelReportEditDto.getTransportCostEditDto()).isEqualTo(transportCostEditDto);
    }
}
