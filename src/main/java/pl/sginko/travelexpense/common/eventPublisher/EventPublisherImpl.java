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
