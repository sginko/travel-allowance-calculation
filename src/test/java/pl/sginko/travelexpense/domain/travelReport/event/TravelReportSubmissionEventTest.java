package pl.sginko.travelexpense.domain.travelReport.event;

import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.domain.travelReport.event.TravelReportSubmissionEvent;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TravelReportSubmissionEventTest {

    @Test
    void should_create_travel_submission_event_with_given_values() {
        // GIVEN
        UUID travelTechId = UUID.randomUUID();
        String userEmail = "user@example.com";
        TravelReportStatus status = TravelReportStatus.SUBMITTED;

        // WHEN
        TravelReportSubmissionEvent event = new TravelReportSubmissionEvent(travelTechId, userEmail, status);

        // THEN
        assertThat(event.getTravelTechId()).isEqualTo(travelTechId);
        assertThat(event.getUserEmail()).isEqualTo(userEmail);
        assertThat(event.getStatus()).isEqualTo(status);
    }
}
