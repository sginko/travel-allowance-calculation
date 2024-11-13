package pl.sginko.travelexpense.logic.travelexpense.travelReport.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.diet.service.DietService;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.service.TransportCostService;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.event.TravelReportSubmissionEvent;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.mapper.TravelReportMapper;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.repository.TravelReportRepository;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.service.userService.UserReaderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TravelReportServiceTest {
    @Mock
    private TravelReportRepository travelReportRepository;

    @Mock
    private TravelReportMapper travelReportMapper;

    @Mock
    private DietService dietService;

    @Mock
    private OvernightStayService overnightStayService;

    @Mock
    private TransportCostService transportCostService;

    @Mock
    private UserReaderService userReaderService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TravelReportServiceImpl travelService;

    @Mock
    private TravelReportRequestDto travelReportRequestDto;

    @Mock
    private DietDto dietDto;

    @Mock
    private OvernightStayDto overnightStayDto;

    @Mock
    private TransportCostDto transportCostDto;

    private UserEntity userEntity;
    private TravelReportEntity travelReportEntity;
    private DietEntity dietEntity;
    private OvernightStayEntity overnightStayEntity;
    private TransportCostEntity transportCostEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("user@example.com", "password"));

        userEntity = new UserEntity("user@example.com", "John", "Doe", "password123");

        travelReportEntity = new TravelReportEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                userEntity, BigDecimal.valueOf(100), BigDecimal.valueOf(50));

        dietEntity = new DietEntity(travelReportEntity, BigDecimal.valueOf(45), 0, 0, 0);

        travelReportEntity.setDietDetails(dietEntity);

        overnightStayEntity = new OvernightStayEntity(travelReportEntity, 1,
                0, BigDecimal.valueOf(500), false);

        transportCostEntity = new TransportCostEntity(travelReportEntity, 1, BigDecimal.valueOf(200),
                "Car", BigDecimal.valueOf(150), 100L, 200L,
                50L, 30L);

        when(travelReportRequestDto.getDietDto()).thenReturn(dietDto);
        when(travelReportRequestDto.getOvernightStayDto()).thenReturn(overnightStayDto);
        when(travelReportRequestDto.getTransportCostDto()).thenReturn(transportCostDto);

        when(dietService.createDietEntity(dietDto, travelReportEntity)).thenReturn(dietEntity);
        when(overnightStayService.createOvernightStayEntity(overnightStayDto, travelReportEntity)).thenReturn(overnightStayEntity);
        when(transportCostService.createTransportCostEntity(transportCostDto, travelReportEntity)).thenReturn(transportCostEntity);

        when(travelReportMapper.toTravelEntity(travelReportRequestDto, userEntity)).thenReturn(travelReportEntity);
        when(travelReportMapper.toTravelSubmissionResponseDto(travelReportEntity)).thenReturn(
                new TravelReportSubmissionResponseDto(travelReportEntity.getTechId(), travelReportEntity.getStatus())
        );

        when(userReaderService.findUserByEmail(anyString())).thenReturn(userEntity);
        when(travelReportRepository.save(travelReportEntity)).thenReturn(travelReportEntity);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void should_calculate_travel_expenses_and_create_travel_submission_response() {
        // WHEN
        TravelReportSubmissionResponseDto response = travelService.createTravelExpenseReport(travelReportRequestDto);

        // THEN
        assertThat(response.getTechId()).isEqualTo(travelReportEntity.getTechId());
        assertThat(response.getStatus()).isEqualTo(travelReportEntity.getStatus());

        verify(dietService, times(1)).createDietEntity(dietDto, travelReportEntity);
        verify(overnightStayService, times(1)).createOvernightStayEntity(overnightStayDto, travelReportEntity);
        verify(transportCostService, times(1)).createTransportCostEntity(transportCostDto, travelReportEntity);
        verify(travelReportRepository, times(1)).save(travelReportEntity);

        ArgumentCaptor<TravelReportSubmissionEvent> eventCaptor = ArgumentCaptor.forClass(TravelReportSubmissionEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        TravelReportSubmissionEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getTravelTechId()).isEqualTo(travelReportEntity.getTechId());
        assertThat(capturedEvent.getUserEmail()).isEqualTo(userEntity.getEmail());
    }

    @Test
    void should_get_all_travels_by_user() {
        when(travelReportRepository.findAllByUserEntity_Email("user@example.com")).thenReturn(Collections.singletonList(travelReportEntity));
        when(travelReportMapper.toResponseDto(travelReportEntity)).thenReturn(
                new TravelReportResponseDto(travelReportEntity.getTechId(), "user@example.com", "CityA",
                        "CityB", LocalDate.now(), LocalTime.of(8, 0), LocalDate.now().plusDays(1),
                        LocalTime.of(18, 0), BigDecimal.valueOf(50), BigDecimal.valueOf(200),
                        BigDecimal.valueOf(100), null, null, null));

        List<TravelReportResponseDto> travels = travelService.getUserTravelExpenseReports();

        assertThat(travels).hasSize(1);
        assertThat(travels.get(0).getFromCity()).isEqualTo("CityA");
    }

    @Test
    void should_get_travel_by_tech_id() {
        UUID techId = UUID.randomUUID();
        when(travelReportRepository.findByTechId(techId)).thenReturn(Optional.of(travelReportEntity));
        when(travelReportMapper.toTravelSubmissionResponseDto(travelReportEntity)).thenReturn(
                new TravelReportSubmissionResponseDto(techId, TravelReportStatus.SUBMITTED)
        );

        TravelReportSubmissionResponseDto response = travelService.getTravelExpenseReportById(techId);

        assertThat(response.getTechId()).isEqualTo(techId);
        assertThat(response.getStatus()).isEqualTo(TravelReportStatus.SUBMITTED);
    }

    @Test
    void should_throw_exception_when_travel_not_found_by_tech_id() {
        UUID techId = UUID.randomUUID();
        when(travelReportRepository.findByTechId(techId)).thenReturn(Optional.empty());

        assertThrows(TravelReportException.class, () -> travelService.getTravelExpenseReportById(techId));
    }

    @Test
    void should_cleanup_old_reports() {
        LocalDate cutoffDate = LocalDate.now().minusDays(1);
        when(travelReportRepository.findByStartDateBefore(cutoffDate)).thenReturn(Collections.singletonList(travelReportEntity));

        travelService.cleanupOldReports();

        verify(travelReportRepository, times(1)).delete(travelReportEntity);
    }
}
