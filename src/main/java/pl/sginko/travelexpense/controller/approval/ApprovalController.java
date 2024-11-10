package pl.sginko.travelexpense.controller.approval;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.domen.approval.service.ApprovalService;
import pl.sginko.travelexpense.domen.auth.util.AuthenticationUtil;
import pl.sginko.travelexpense.domen.travelexpense.travel.dto.TravelResponseDto;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/approvals")
public class ApprovalController {
    private final ApprovalService approvalService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pending")
    public List<TravelResponseDto> getPendingApprovals() {
        String approverEmail = AuthenticationUtil.getCurrentUserEmail();
        return approvalService.getPendingApprovals(approverEmail);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{travelId}/approve")
    public void approveTravel(@PathVariable UUID travelId) {
        String approverEmail = AuthenticationUtil.getCurrentUserEmail();
        approvalService.approveTravel(travelId, approverEmail);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{travelId}/reject")
    public void rejectTravel(@PathVariable UUID travelId) {
        String approverEmail = AuthenticationUtil.getCurrentUserEmail();
        approvalService.rejectTravel(travelId, approverEmail);
    }
}
