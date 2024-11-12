package pl.sginko.travelexpense.logic.travelexpense.travel.service;

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
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelSubmissionResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelStatus;
import pl.sginko.travelexpense.logic.travelexpense.travel.event.TravelSubmissionEvent;
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;
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

class TravelServiceTest {
    @Mock
    private TravelRepository travelRepository;

    @Mock
    private TravelMapper travelMapper;

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
    private TravelServiceImpl travelService;

    @Mock
    private TravelRequestDto travelRequestDto;

    @Mock
    private DietDto dietDto;

    @Mock
    private OvernightStayDto overnightStayDto;

    @Mock
    private TransportCostDto transportCostDto;

    private UserEntity userEntity;
    private TravelEntity travelEntity;
    private DietEntity dietEntity;
    private OvernightStayEntity overnightStayEntity;
    private TransportCostEntity transportCostEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("user@example.com", "password"));

        userEntity = new UserEntity("user@example.com", "John", "Doe", "password123");

        travelEntity = new TravelEntity("CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                userEntity, BigDecimal.valueOf(100), BigDecimal.valueOf(50));

        dietEntity = new DietEntity(travelEntity, BigDecimal.valueOf(45), 0, 0, 0);

        travelEntity.updateDietEntity(dietEntity);

        overnightStayEntity = new OvernightStayEntity(travelEntity, 1,
                0, BigDecimal.valueOf(500), false);

        transportCostEntity = new TransportCostEntity(travelEntity, 1, BigDecimal.valueOf(200),
                "Car", BigDecimal.valueOf(150), 100L, 200L,
                50L, 30L);

        when(travelRequestDto.getDietDto()).thenReturn(dietDto);
        when(travelRequestDto.getOvernightStayDto()).thenReturn(overnightStayDto);
        when(travelRequestDto.getTransportCostDto()).thenReturn(transportCostDto);

        when(dietService.createDietEntity(dietDto, travelEntity)).thenReturn(dietEntity);
        when(overnightStayService.createOvernightStayEntity(overnightStayDto, travelEntity)).thenReturn(overnightStayEntity);
        when(transportCostService.createTransportCostEntity(transportCostDto, travelEntity)).thenReturn(transportCostEntity);

        when(travelMapper.toTravelEntity(travelRequestDto, userEntity)).thenReturn(travelEntity);
        when(travelMapper.toTravelSubmissionResponseDto(travelEntity)).thenReturn(
                new TravelSubmissionResponseDto(travelEntity.getTechId(), travelEntity.getStatus())
        );

        when(userReaderService.findUserByEmail(anyString())).thenReturn(userEntity);
        when(travelRepository.save(travelEntity)).thenReturn(travelEntity);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void should_calculate_travel_expenses_and_create_travel_submission_response() {
        // WHEN
        TravelSubmissionResponseDto response = travelService.calculateTravelExpenses(travelRequestDto);

        // THEN
        assertThat(response.getTechId()).isEqualTo(travelEntity.getTechId());
        assertThat(response.getStatus()).isEqualTo(travelEntity.getStatus());

        verify(dietService, times(1)).createDietEntity(dietDto, travelEntity);
        verify(overnightStayService, times(1)).createOvernightStayEntity(overnightStayDto, travelEntity);
        verify(transportCostService, times(1)).createTransportCostEntity(transportCostDto, travelEntity);
        verify(travelRepository, times(1)).save(travelEntity);

        ArgumentCaptor<TravelSubmissionEvent> eventCaptor = ArgumentCaptor.forClass(TravelSubmissionEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        TravelSubmissionEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getTravelTechId()).isEqualTo(travelEntity.getTechId());
        assertThat(capturedEvent.getUserEmail()).isEqualTo(userEntity.getEmail());
    }

    @Test
    void should_get_all_travels_by_user() {
        when(travelRepository.findAllByUserEntity_Email("user@example.com")).thenReturn(Collections.singletonList(travelEntity));
        when(travelMapper.toResponseDto(travelEntity)).thenReturn(
                new TravelResponseDto(travelEntity.getTechId(), "user@example.com", "CityA",
                        "CityB", LocalDate.now(), LocalTime.of(8, 0), LocalDate.now().plusDays(1),
                        LocalTime.of(18, 0), BigDecimal.valueOf(50), BigDecimal.valueOf(200),
                        BigDecimal.valueOf(100), null, null, null));

        List<TravelResponseDto> travels = travelService.getAllTravelsByUser();

        assertThat(travels).hasSize(1);
        assertThat(travels.get(0).getFromCity()).isEqualTo("CityA");
    }

    @Test
    void should_get_travel_by_tech_id() {
        UUID techId = UUID.randomUUID();
        when(travelRepository.findByTechId(techId)).thenReturn(Optional.of(travelEntity));
        when(travelMapper.toTravelSubmissionResponseDto(travelEntity)).thenReturn(
                new TravelSubmissionResponseDto(techId, TravelStatus.SUBMITTED)
        );

        TravelSubmissionResponseDto response = travelService.getTravelByTechId(techId);

        assertThat(response.getTechId()).isEqualTo(techId);
        assertThat(response.getStatus()).isEqualTo(TravelStatus.SUBMITTED);
    }

    @Test
    void should_throw_exception_when_travel_not_found_by_tech_id() {
        UUID techId = UUID.randomUUID();
        when(travelRepository.findByTechId(techId)).thenReturn(Optional.empty());

        assertThrows(TravelException.class, () -> travelService.getTravelByTechId(techId));
    }

    @Test
    void should_cleanup_old_reports() {
        LocalDate cutoffDate = LocalDate.now().minusDays(1);
        when(travelRepository.findByStartDateBefore(cutoffDate)).thenReturn(Collections.singletonList(travelEntity));

        travelService.cleanupOldReports();

        verify(travelRepository, times(1)).delete(travelEntity);
    }
}
