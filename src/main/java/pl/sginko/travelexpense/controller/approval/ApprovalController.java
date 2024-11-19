package pl.sginko.travelexpense.controller.approval;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.common.util.AuthenticationUtil;
import pl.sginko.travelexpense.domain.approval.service.ApprovalService;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/approvals")
public class ApprovalController {
    private final ApprovalService approvalService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pending")
    public List<TravelReportResponseDto> getTravelReportsForApproval() {
        String approverEmail = AuthenticationUtil.getCurrentUserEmail();
        return approvalService.getTravelReportsForApproval(approverEmail);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{travelId}/approve")
    public void approveTravel(@PathVariable UUID travelId) {
        String approverEmail = AuthenticationUtil.getCurrentUserEmail();
        approvalService.approveTravelReport(travelId, approverEmail);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{travelId}/reject")
    public void rejectTravel(@PathVariable UUID travelId) {
        String approverEmail = AuthenticationUtil.getCurrentUserEmail();
        approvalService.rejectTravelReport(travelId, approverEmail);
    }
}
