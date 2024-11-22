package pl.sginko.travelexpense.controller.travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.sginko.travelexpense.common.pdfDocument.exception.PdfDocumentException;
import pl.sginko.travelexpense.common.pdfDocument.service.PdfDocumentService;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietDto;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayDto;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportRequestDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportNotFoundException;
import pl.sginko.travelexpense.domain.travelReport.service.TravelReportService;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TravelReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TravelReportService travelReportService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private PdfDocumentService pdfDocumentService;

    // POST /api/v1/travels
    // HAPPY PATH
    @Test
    @WithMockUser
    public void should_create_travel_expense_report_successfully() throws Exception {
        // GIVEN
        TravelReportRequestDto requestDto = new TravelReportRequestDto("CityA", "CityB",
                LocalDate.of(2024, 11, 15), LocalTime.of(8, 0),
                LocalDate.of(2024, 11, 16), LocalTime.of(18, 0),
                BigDecimal.valueOf(500.00), BigDecimal.valueOf(100.00),
                new DietDto(BigDecimal.valueOf(50), 2, 1, 1),
                new OvernightStayDto(2, 1,
                        BigDecimal.valueOf(150.00), true),
                new TransportCostDto(1, BigDecimal.valueOf(200.00), "Car",
                        BigDecimal.valueOf(100.00), 100L, 0L,
                        0L, 0L));

        TravelReportSubmissionResponseDto responseDto = new TravelReportSubmissionResponseDto(UUID.randomUUID(), TravelReportStatus.SUBMITTED);

        when(travelReportService.createTravelExpenseReport(any())).thenReturn(responseDto);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/travels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.techId").isNotEmpty())
                .andExpect(jsonPath("$.status").value("SUBMITTED"));

        verify(travelReportService, times(1)).createTravelExpenseReport(any());
    }

    // GET /api/v1/travels
    // HAPPY PATH
    @Test
    @WithMockUser
    public void should_get_all_travel_expense_reports_successfully() throws Exception {
        // GIVEN
        List<TravelReportResponseDto> responseList = List.of(
                new TravelReportResponseDto(UUID.randomUUID(), "user1@test.com", "CityA", "CityB",
                        LocalDate.of(2024, 11, 15), LocalTime.of(8, 0),
                        LocalDate.of(2024, 11, 16), LocalTime.of(18, 0),
                        BigDecimal.valueOf(150.00), BigDecimal.valueOf(800.00), BigDecimal.valueOf(500.00),
                        new DietResponseDto(1L, 2, 1, 1,
                                BigDecimal.valueOf(50.00), BigDecimal.valueOf(100.00)),
                        new OvernightStayResponseDto(1L, 2, 2,
                                1, BigDecimal.valueOf(100.00), 1,
                                BigDecimal.valueOf(150.00), BigDecimal.valueOf(250.00)),
                        new TransportCostResponseDto(1L, 2, BigDecimal.valueOf(20.00),
                                BigDecimal.valueOf(50.00), "Car", BigDecimal.valueOf(100.00),
                                BigDecimal.valueOf(150.00), BigDecimal.valueOf(300.00))),

                new TravelReportResponseDto(UUID.randomUUID(), "user2@test.com", "CityC", "CityD",
                        LocalDate.of(2024, 11, 17), LocalTime.of(9, 0),
                        LocalDate.of(2024, 11, 18), LocalTime.of(19, 0),
                        BigDecimal.valueOf(200.00), BigDecimal.valueOf(1000.00), BigDecimal.valueOf(700.00),
                        new DietResponseDto(2L, 3, 2, 1,
                                BigDecimal.valueOf(60.00), BigDecimal.valueOf(120.00)),
                        new OvernightStayResponseDto(2L, 3, 3,
                                2, BigDecimal.valueOf(150.00), 1,
                                BigDecimal.valueOf(180.00), BigDecimal.valueOf(330.00)),
                        new TransportCostResponseDto(2L, 3, BigDecimal.valueOf(30.00),
                                BigDecimal.valueOf(60.00), "Train", BigDecimal.valueOf(200.00),
                                BigDecimal.valueOf(250.00), BigDecimal.valueOf(510.00))));

        when(travelReportService.getUserTravelExpenseReports()).thenReturn(responseList);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/travels")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$[1].email").value("user2@test.com"))
                .andExpect(jsonPath("$[0].fromCity").value("CityA"))
                .andExpect(jsonPath("$[1].fromCity").value("CityC"))
                .andExpect(jsonPath("$[0].totalAmount").value(800.00))
                .andExpect(jsonPath("$[1].totalAmount").value(1000.00));

        verify(travelReportService, times(1)).getUserTravelExpenseReports();
    }

    // GET /api/v1/travels
    // UNHAPPY PATH
    @Test
    @WithMockUser
    public void should_return_empty_list_when_no_travel_expense_reports_exist() throws Exception {
        // GIVEN
        when(travelReportService.getUserTravelExpenseReports()).thenReturn(List.of());

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/travels")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(travelReportService, times(1)).getUserTravelExpenseReports();
    }

    // POST /api/v1/travels/print/{techId}
    // HAPPY PATH
    @Test
    @WithMockUser
    public void should_generate_pdf_successfully() throws Exception {
        // GIVEN
        UUID techId = UUID.randomUUID();
        ByteArrayOutputStream mockPdfStream = new ByteArrayOutputStream();
        mockPdfStream.write("Mock PDF Content".getBytes());

        when(pdfDocumentService.generateTravelExpenseReportPdfAsStream(techId))
                .thenReturn(mockPdfStream);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/travels/print/{techId}", techId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_PDF_VALUE))
                .andExpect(header().string("Content-Disposition", "inline; filename=travel_expense_report.pdf"));

        verify(pdfDocumentService, times(1)).generateTravelExpenseReportPdfAsStream(techId);
    }

    // POST /api/v1/travels/print/{techId}
    // UNHAPPY PATH
    @Test
    @WithMockUser
    public void should_return_internal_server_error_when_pdf_generation_fails() throws Exception {
        // GIVEN
        UUID techId = UUID.randomUUID();

        when(pdfDocumentService.generateTravelExpenseReportPdfAsStream(techId))
                .thenThrow(new PdfDocumentException("Failed to generate PDF"));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/travels/print/{techId}", techId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(pdfDocumentService, times(1)).generateTravelExpenseReportPdfAsStream(techId);
    }

    // POST /api/v1/travels/print/{techId}
    // UNHAPPY PATH
    @Test
    @WithMockUser
    public void should_return_not_found_when_pdf_generation_fails_due_to_invalid_id() throws Exception {
        // GIVEN
        UUID nonExistentTechId = UUID.randomUUID();

        when(pdfDocumentService.generateTravelExpenseReportPdfAsStream(nonExistentTechId))
                .thenThrow(new TravelReportNotFoundException("Travel report not found"));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/travels/print/{techId}", nonExistentTechId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(pdfDocumentService, times(1)).generateTravelExpenseReportPdfAsStream(nonExistentTechId);
    }

    // GET /api/v1/travels/{techId}
    // HAPPY PATH
    @Test
    @WithMockUser
    public void should_return_travel_report_by_id_successfully() throws Exception {
        // GIVEN
        UUID techId = UUID.randomUUID();
        TravelReportSubmissionResponseDto responseDto = new TravelReportSubmissionResponseDto(techId, TravelReportStatus.SUBMITTED);

        when(travelReportService.getTravelExpenseReportById(techId)).thenReturn(responseDto);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/travels/{techId}", techId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.techId").value(techId.toString()))
                .andExpect(jsonPath("$.status").value("SUBMITTED"));

        verify(travelReportService, times(1)).getTravelExpenseReportById(techId);
    }

    // GET /api/v1/travels/{techId}
    // UNHAPPY PATH
    @Test
    @WithMockUser
    public void should_return_not_found_when_travel_report_not_exist() throws Exception {
        // GIVEN
        UUID techId = UUID.randomUUID();

        when(travelReportService.getTravelExpenseReportById(techId))
                .thenThrow(new TravelReportNotFoundException("Travel report not found"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/travels/{techId}", techId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Travel report not found"));

        verify(travelReportService, times(1)).getTravelExpenseReportById(techId);
    }

    // PATCH /api/v1/travels/update/{techId}
    // HAPPY PATH
    @Test
    @WithMockUser
    public void should_update_travel_report_successfully() throws Exception {
        // GIVEN
        UUID techId = UUID.randomUUID();
        JsonPatch patch = JsonPatch.fromJson(objectMapper.readTree("[{\"op\": \"replace\", \"path\": \"/fromCity\", \"value\": \"NewCity\"}]"));

        doNothing().when(travelReportService).updateTravelExpenseReportById(eq(techId), any(JsonPatch.class));

        // WHEN & THEN
        mockMvc.perform(patch("/api/v1/travels/update/{techId}", techId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk());

        verify(travelReportService, times(1)).updateTravelExpenseReportById(eq(techId), any(JsonPatch.class));
    }

    // PATCH /api/v1/travels/update/{techId}
   // UNHAPPY PATH
    @Test
    @WithMockUser
    public void should_return_bad_request_when_invalid_patch_provided() throws Exception {
        // GIVEN
        UUID techId = UUID.randomUUID();
        String invalidPatch = "[{\"op\": \"invalid\", \"path\": \"/fromCity\", \"value\": \"NewCity\"}]";

        // WHEN & THEN
        mockMvc.perform(patch("/api/v1/travels/update/{techId}", techId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPatch))
                .andExpect(status().isBadRequest());
    }

    // POST /api/v1/travels/print/{techId}
    // UNHAPPY PATH
    @Test
    @WithMockUser
    public void should_return_not_found_when_travel_report_exception_thrown() throws Exception {
        // GIVEN
        UUID techId = UUID.randomUUID();

        when(pdfDocumentService.generateTravelExpenseReportPdfAsStream(techId))
                .thenThrow(new TravelReportException("Travel report not found"));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/travels/print/{techId}", techId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(pdfDocumentService, times(1)).generateTravelExpenseReportPdfAsStream(techId);
    }
}
