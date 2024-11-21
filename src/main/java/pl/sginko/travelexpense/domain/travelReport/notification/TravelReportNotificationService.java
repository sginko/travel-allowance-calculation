package pl.sginko.travelexpense.domain.travelReport.notification;

import pl.sginko.travelexpense.domain.travelReport.event.TravelReportSubmissionEvent;

public interface TravelReportNotificationService {

    void handleTravelReportSubmissionEvent(TravelReportSubmissionEvent event);
}
