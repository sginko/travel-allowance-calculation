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
package pl.sginko.travelexpense.common.notification.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.common.notification.dto.EmailNotificationDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

import java.util.UUID;

@Component
public class NotificationMapper {

    public EmailNotificationDto toEmailNotificationDto(UUID travelTechId, String userEmail, TravelReportStatus status) {
        String subject = "Travel Expense Report " + status.name();
        String body = buildEmailBody(travelTechId, status.name());
        return new EmailNotificationDto(userEmail, subject, body);
    }

    private String buildEmailBody(UUID travelTechId, String status) {
        return "Dear User,\n\n" +
                "Your travel expense report with ID " + travelTechId + " has been " + status.toLowerCase() + ".\n\n" +
                "Best regards,\n" +
                "Your company";
    }
}
