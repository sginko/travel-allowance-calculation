package pl.sginko.travelexpense.domain.approval.service;

import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;

import java.util.List;
import java.util.UUID;

public interface ApprovalService {

    List<TravelReportResponseDto> getTravelReportsForApproval(String approverEmail);

    void approveTravelReport(UUID travelId, String approverEmail);

    void rejectTravelReport(UUID travelId, String approverEmail);
}
