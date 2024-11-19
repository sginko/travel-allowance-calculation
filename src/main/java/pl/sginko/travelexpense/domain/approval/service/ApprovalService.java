package pl.sginko.travelexpense.domain.approval.service;

import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;

import java.util.List;
import java.util.UUID;

public interface ApprovalService {

    List<TravelReportResponseDto> getPendingApprovals(String approverEmail);

    void approveTravel(UUID travelId, String approverEmail);

    void rejectTravel(UUID travelId, String approverEmail);
}
