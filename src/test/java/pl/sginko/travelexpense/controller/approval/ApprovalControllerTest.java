package pl.sginko.travelexpense.controller.approval;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.sginko.travelexpense.logic.approval.exception.ApprovalException;
import pl.sginko.travelexpense.logic.approval.service.ApprovalService;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.exception.TravelReportNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApprovalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApprovalService approvalService;

    @MockBean
    private JavaMailSender javaMailSender;

    // GET /api/v1/approvals/pending
    // HAPPY PATH
    @Test
    @WithMockUser(username = "approver@test.com", roles = {"ACCOUNTANT"})
    public void should_return_pending_approvals_for_current_user() throws Exception {
        // GIVEN
        String approverEmail = "approver@test.com";
        List<TravelReportResponseDto> mockPendingApprovals = List.of(
                new TravelReportResponseDto(UUID.randomUUID(), "user1@test.com", "CityA", "CityB",
                        LocalDate.of(2024, 11, 15), LocalTime.of(8, 0),
                        LocalDate.of(2024, 11, 16), LocalTime.of(18, 0),
                        BigDecimal.valueOf(150.00), BigDecimal.valueOf(800.00), BigDecimal.valueOf(500.00),
                        new DietResponseDto(1L, 2, 1, 1,
                                BigDecimal.valueOf(50.00), BigDecimal.valueOf(100.00)),
                        new OvernightStayResponseDto(1L, 2, 2,
                                1, BigDecimal.valueOf(100.00), 1,
                                BigDecimal.valueOf(150.00), BigDecimal.valueOf(250.00)),
                        new TransportCostResponseDto(1L, 2, BigDecimal.valueOf(20.00), BigDecimal.valueOf(50.00),
                                "Car", BigDecimal.valueOf(100.00), BigDecimal.valueOf(150.00), BigDecimal.valueOf(300.00)))
        );

        when(approvalService.getPendingApprovals(approverEmail)).thenReturn(mockPendingApprovals);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/approvals/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fromCity").value("CityA"))
                .andExpect(jsonPath("$[0].toCity").value("CityB"));

        verify(approvalService, times(1)).getPendingApprovals(approverEmail);
    }

    // GET /api/v1/approvals/pending
    // UNHAPPY PATH
    @Test
    @WithMockUser(username = "approver@test.com", roles = {"ACCOUNTANT"})
    public void should_return_error_when_no_pending_approvals_found() throws Exception {
        // GIVEN
        String approverEmail = "approver@test.com";

        when(approvalService.getPendingApprovals(approverEmail))
                .thenThrow(new ApprovalException("No pending approvals found"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/approvals/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No pending approvals found"));

        verify(approvalService, times(1)).getPendingApprovals(approverEmail);
    }

    // POST /api/v1/approvals/{travelId}/approve
    // HAPPY PATH
    @Test
    @WithMockUser(username = "approver@test.com", roles = {"ACCOUNTANT"})
    public void should_approve_travel_successfully() throws Exception {
        // GIVEN
        UUID travelId = UUID.randomUUID();
        String approverEmail = "approver@test.com";

        doNothing().when(approvalService).approveTravel(travelId, approverEmail);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/approvals/{travelId}/approve", travelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(approvalService, times(1)).approveTravel(travelId, approverEmail);
    }

    // POST /api/v1/approvals/{travelId}/reject
    // HAPPY PATH
    @Test
    @WithMockUser(username = "approver@test.com", roles = {"ACCOUNTANT"})
    public void should_reject_travel_successfully() throws Exception {
        // GIVEN
        UUID travelId = UUID.randomUUID();
        String approverEmail = "approver@test.com";

        doNothing().when(approvalService).rejectTravel(travelId, approverEmail);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/approvals/{travelId}/reject", travelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(approvalService, times(1)).rejectTravel(travelId, approverEmail);
    }

    // POST /api/v1/approvals/{travelId}/approve
    // UNHAPPY PATH
    @Test
    @WithMockUser(username = "approver@test.com", roles = {"ACCOUNTANT"})
    public void should_return_error_when_approving_nonexistent_travel() throws Exception {
        // GIVEN
        UUID travelId = UUID.randomUUID();
        String approverEmail = "approver@test.com";

        doThrow(new TravelReportNotFoundException("Travel not found"))
                .when(approvalService).approveTravel(travelId, approverEmail);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/approvals/{travelId}/approve", travelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Travel not found"));

        verify(approvalService, times(1)).approveTravel(travelId, approverEmail);
    }

    // POST /api/v1/approvals/{travelId}/reject
    // UNHAPPY PATH
    @Test
    @WithMockUser(username = "approver@test.com", roles = {"ACCOUNTANT"})
    public void should_return_error_when_rejecting_nonexistent_travel() throws Exception {
        // GIVEN
        UUID travelId = UUID.randomUUID();
        String approverEmail = "approver@test.com";

        doThrow(new TravelReportNotFoundException("Travel not found"))
                .when(approvalService).rejectTravel(travelId, approverEmail);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/approvals/{travelId}/reject", travelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Travel not found"));

        verify(approvalService, times(1)).rejectTravel(travelId, approverEmail);
    }
}
