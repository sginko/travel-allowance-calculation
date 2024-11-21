package pl.sginko.travelexpense.common.eventPublisher;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.domain.approval.event.TravelReportApprovalEvent;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.domain.travelReport.event.TravelReportSubmissionEvent;

import java.util.UUID;

@AllArgsConstructor
@Service
public class EventPublisherImpl implements EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishTravelReportSubmissionEvent(UUID travelTechId, String userEmail, TravelReportStatus status) {
        TravelReportSubmissionEvent event = new TravelReportSubmissionEvent(travelTechId, userEmail, status);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publishTravelReportApprovalEvent(UUID travelTechId, String userEmail, TravelReportStatus status) {
        TravelReportApprovalEvent event = new TravelReportApprovalEvent(travelTechId, userEmail, status);
        eventPublisher.publishEvent(event);
    }
}
