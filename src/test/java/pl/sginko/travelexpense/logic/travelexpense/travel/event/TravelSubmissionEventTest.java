package pl.sginko.travelexpense.logic.travelexpense.travel.event;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TravelSubmissionEventTest {

    @Test
    void should_create_travel_submission_event_with_given_values() {
        // GIVEN
        UUID travelTechId = UUID.randomUUID();
        String userEmail = "user@example.com";

        // WHEN
        TravelSubmissionEvent event = new TravelSubmissionEvent(travelTechId, userEmail);

        // THEN
        assertThat(event.getTravelTechId()).isEqualTo(travelTechId);
        assertThat(event.getUserEmail()).isEqualTo(userEmail);
    }
}
