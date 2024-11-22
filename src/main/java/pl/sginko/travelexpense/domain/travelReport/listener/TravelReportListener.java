package pl.sginko.travelexpense.domain.travelReport.listener;

import pl.sginko.travelexpense.domain.travelReport.event.TravelReportSubmissionEvent;

public interface TravelReportListener {

    void handleTravelReportSubmissionEvent(TravelReportSubmissionEvent event);
}
