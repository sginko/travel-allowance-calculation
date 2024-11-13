package pl.sginko.travelexpense.logic.approval.service;

import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportResponseDto;

import java.util.List;
import java.util.UUID;

public interface ApprovalService {

    List<TravelReportResponseDto> getPendingApprovals(String approverEmail);

    void approveTravel(UUID travelId, String approverEmail);

    void rejectTravel(UUID travelId, String approverEmail);
}
