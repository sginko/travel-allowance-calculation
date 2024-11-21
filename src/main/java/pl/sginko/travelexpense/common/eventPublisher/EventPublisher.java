package pl.sginko.travelexpense.common.eventPublisher;

import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

import java.util.UUID;

public interface EventPublisher {

    void publishTravelReportSubmissionEvent(UUID travelTechId, String userEmail, TravelReportStatus status);

    void publishTravelReportApprovalEvent(UUID travelTechId, String userEmail, TravelReportStatus status);
}
