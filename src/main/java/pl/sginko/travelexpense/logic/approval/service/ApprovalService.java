package pl.sginko.travelexpense.logic.approval.service;

import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;

import java.util.List;
import java.util.UUID;

public interface ApprovalService {

    List<TravelResponseDto> getPendingApprovals(String approverEmail);

    void approveTravel(UUID travelId, String approverEmail);

    void rejectTravel(UUID travelId, String approverEmail);
}
