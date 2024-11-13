package pl.sginko.travelexpense.logic.travelexpense.travel.mapper;

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
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelSubmissionResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelStatus;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TravelMapperTest {
    @InjectMocks
    private TravelMapper travelMapper;

    @Mock
    private DietMapper dietMapper;

    @Mock
    private OvernightStayMapper overnightStayMapper;

    @Mock
    private TransportCostMapper transportCostMapper;

    private TravelRequestDto travelRequestDto;
    private UserEntity userEntity;
    private TravelEntity travelEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity("user@example.com", "John", "Doe", "password123");

        travelRequestDto = new TravelRequestDto("CityA", "CityB", LocalDate.now(),
                LocalTime.of(8, 0), LocalDate.now().plusDays(2), LocalTime.of(18, 0),
                BigDecimal.valueOf(100), BigDecimal.valueOf(50), null, null, null);

        travelEntity = new TravelEntity("CityA", "CityB", LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0), userEntity, BigDecimal.valueOf(100),
                BigDecimal.valueOf(50));
    }

    @Test
    void should_map_TravelRequestDto_to_TravelEntity_correctly() {
        // WHEN
        TravelEntity mappedEntity = travelMapper.toTravelEntity(travelRequestDto, userEntity);

        // THEN
        assertThat(mappedEntity).isNotNull();
        assertThat(mappedEntity.getFromCity()).isEqualTo("CityA");
        assertThat(mappedEntity.getToCity()).isEqualTo("CityB");
        assertThat(mappedEntity.getStartDate()).isEqualTo(travelRequestDto.getStartDate());
        assertThat(mappedEntity.getStartTime()).isEqualTo(travelRequestDto.getStartTime());
        assertThat(mappedEntity.getEndDate()).isEqualTo(travelRequestDto.getEndDate());
        assertThat(mappedEntity.getEndTime()).isEqualTo(travelRequestDto.getEndTime());
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

        when(dietMapper.toResponseDto(travelEntity.getDietEntity())).thenReturn(dietResponseDto);
        when(overnightStayMapper.toResponseDto(travelEntity.getOvernightStayEntity())).thenReturn(overnightStayResponseDto);
        when(transportCostMapper.toResponseDto(travelEntity.getTransportCostEntity())).thenReturn(transportCostResponseDto);

        // WHEN
        TravelResponseDto responseDto = travelMapper.toResponseDto(travelEntity);

        // THEN
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getFromCity()).isEqualTo(travelEntity.getFromCity());
        assertThat(responseDto.getToCity()).isEqualTo(travelEntity.getToCity());
        assertThat(responseDto.getEmail()).isEqualTo(travelEntity.getUserEntity().getEmail());
        assertThat(responseDto.getDietResponse()).isEqualTo(dietResponseDto);
        assertThat(responseDto.getOvernightStayResponseDto()).isEqualTo(overnightStayResponseDto);
        assertThat(responseDto.getTransportCostResponseDto()).isEqualTo(transportCostResponseDto);
    }

    @Test
    void should_map_TravelEntity_to_TravelSubmissionResponseDto_correctly() {
        // WHEN
        travelEntity.updateStatus(TravelStatus.APPROVED);
        TravelSubmissionResponseDto submissionResponseDto = travelMapper.toTravelSubmissionResponseDto(travelEntity);

        // THEN
        assertThat(submissionResponseDto).isNotNull();
        assertThat(submissionResponseDto.getStatus()).isEqualTo(TravelStatus.APPROVED);
    }
}
