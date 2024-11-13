package pl.sginko.travelexpense.logic.travelexpense.travelReport.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;

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
}
