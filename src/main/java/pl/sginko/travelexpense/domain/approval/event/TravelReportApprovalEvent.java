package pl.sginko.travelexpense.domain.approval.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TravelReportApprovalEvent {
    private final UUID travelTechId;
    private final String userEmail;
    private final TravelReportStatus status;
}
