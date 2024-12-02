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
package pl.sginko.travelexpense.domain.approval.listener;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.common.emailService.EmailService;
import pl.sginko.travelexpense.common.notification.dto.EmailNotificationDto;
import pl.sginko.travelexpense.common.notification.mapper.NotificationMapper;
import pl.sginko.travelexpense.common.notification.service.GlobalNotificationService;
import pl.sginko.travelexpense.domain.approval.event.TravelReportApprovalEvent;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

import java.util.UUID;

@AllArgsConstructor
@Service
public class ApproverListenerImpl implements ApproverListener, GlobalNotificationService {
    private final EmailService emailService;
    private final NotificationMapper notificationMapper;

    @EventListener
    @Override
    public void handleApprovalEvent(TravelReportApprovalEvent event) {
        sendEmailNotification(event.getTravelTechId(), event.getUserEmail(), event.getStatus());
    }

    @Override
    public void sendEmailNotification(UUID travelTechId, String userEmail, TravelReportStatus status) {
        EmailNotificationDto emailNotificationDto = notificationMapper.toEmailNotificationDto(travelTechId, userEmail, status);
        emailService.sendEmail(emailNotificationDto);
    }
}
