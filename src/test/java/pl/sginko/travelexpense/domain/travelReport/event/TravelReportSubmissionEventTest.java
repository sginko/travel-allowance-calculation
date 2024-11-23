/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.domain.travelReport.event;

import org.junit.jupiter.api.Test;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

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
