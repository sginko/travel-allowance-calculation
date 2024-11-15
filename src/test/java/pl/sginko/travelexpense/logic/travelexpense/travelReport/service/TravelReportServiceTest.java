package pl.sginko.travelexpense.logic.travelexpense.travelReport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.repository.TravelReportRepository;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.repository.UserRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TravelReportServiceTest {
    @Autowired
    private TravelReportService travelReportService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TravelReportRepository travelReportRepository;

    @MockBean
    private ApplicationEventPublisher eventPublisher;

    @MockBean
    private JavaMailSender javaMailSender;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity("user@test.com", "name", "surname", "password");

        userRepository.save(userEntity);

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("user@test.com", "password"));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void should_create_travelExpenseReport() {
        // GIVEN WHEN
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(2);

        TravelReportSubmissionResponseDto responseDto = createAndSaveTravelReport("CityA", "CityB",
                startDate, endDate);

        // THEN
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTechId()).isNotNull();
        assertThat(responseDto.getStatus().name()).isEqualTo("SUBMITTED");

        TravelReportEntity savedEntity = travelReportRepository.findByTechId(responseDto.getTechId())
                .orElseThrow(() -> new RuntimeException("Travel report not found"));

        assertThat(savedEntity.getFromCity()).isEqualTo("CityA");
        assertThat(savedEntity.getToCity()).isEqualTo("CityB");
        assertThat(savedEntity.getUserEntity().getEmail()).isEqualTo(userEntity.getEmail());
    }

    @Test
    void should_find_user_travelExpenseReports() {
        // GIVEN
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(2);

        createAndSaveTravelReport("CityA", "CityB", startDate, endDate);
        createAndSaveTravelReport("CityC", "CityD", startDate, endDate);

        // WHEN
        List<TravelReportResponseDto> responseDto = travelReportService.getUserTravelExpenseReports();

        // THEN
        assertThat(responseDto).isNotNull();
        assertThat(responseDto).hasSize(2);
        assertThat(responseDto)
                .extracting(travelReportResponseDto -> travelReportResponseDto.getFromCity())
                .containsExactlyInAnyOrder("CityA", "CityC");
    }

    @Test
    void should_find_travelExpenseReport_by_id() {
        // GIVEN
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(2);

        TravelReportSubmissionResponseDto createdReport = createAndSaveTravelReport("CityA", "CityB",
                startDate, endDate);

        // WHEN
        TravelReportSubmissionResponseDto responseDto = travelReportService.getTravelExpenseReportById(createdReport.getTechId());

        // THEN
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTechId()).isEqualTo(createdReport.getTechId());
        assertThat(responseDto.getStatus()).isEqualTo(createdReport.getStatus());
    }

    @Test
    void testUpdateTravelExpenseReportById() throws JsonPatchException, IOException {
        // GIVEN
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(2);

        TravelReportSubmissionResponseDto createdReport = createAndSaveTravelReport("CityA", "CityB",
                startDate, endDate);

        UUID techId = createdReport.getTechId();

        String patchString = "[{ \"op\": \"replace\", \"path\": \"/fromCity\", \"value\": \"NewCityA\" }]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchString));

        // WHEN
        travelReportService.updateTravelExpenseReportById(techId, patch);

        // THEN
        TravelReportEntity updatedEntity = travelReportRepository.findByTechId(techId)
                .orElseThrow(() -> new RuntimeException("Travel report not found"));
        assertThat(updatedEntity.getFromCity()).isEqualTo("NewCityA");
    }

    @Test
    void should_cleanup_old_reports() {
        // GIVEN
        LocalDate oldStartDate = LocalDate.now().minusDays(3);
        LocalDate oldEndDate = LocalDate.now().minusDays(2);

        createAndSaveTravelReport("CityOld", "CityOld", oldStartDate, oldEndDate);

        List<TravelReportEntity> reportsBeforeCleanup = travelReportRepository.findAll();
        assertThat(reportsBeforeCleanup).hasSize(1);

        // WHEN
        travelReportService.cleanupOldReports();

        // THEN
        List<TravelReportEntity> reportsAfterCleanup = travelReportRepository.findAll();
        assertEquals(0, reportsAfterCleanup.size());
    }

    private TravelReportSubmissionResponseDto createAndSaveTravelReport(String fromCity, String toCity, LocalDate startDate, LocalDate endDate) {
        DietDto dietDto = new DietDto(BigDecimal.valueOf(45), 2, 1, 1);

        OvernightStayDto overnightStayDto = new OvernightStayDto(1,
                0, BigDecimal.ZERO, false);

        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "Car", BigDecimal.valueOf(150), 100L,
                0L, 0L, 0L);

        TravelReportRequestDto requestDto = new TravelReportRequestDto(fromCity, toCity, startDate, LocalTime.of(8, 0),
                endDate, LocalTime.of(18, 0), BigDecimal.valueOf(100), BigDecimal.valueOf(50),
                dietDto, overnightStayDto, transportCostDto);

        return travelReportService.createTravelExpenseReport(requestDto);
    }
}
