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
package pl.sginko.travelexpense.common.emailService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pl.sginko.travelexpense.common.notification.dto.EmailNotificationDto;
import pl.sginko.travelexpense.common.notification.mapper.NotificationMapper;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void should_send_submission_notification_successfully() {
        // GIVEN
        String toEmail = "user@test.com";
        UUID travelTechId = UUID.randomUUID();
        TravelReportStatus status = TravelReportStatus.SUBMITTED;

        EmailNotificationDto emailNotificationDto = new EmailNotificationDto(
                toEmail,
                "Travel Expense Report SUBMITTED",
                "Dear User,\n\nYour travel expense report with ID " + travelTechId + " has been submitted.\n\nBest regards,\nYour company");

        when(notificationMapper.toEmailNotificationDto(travelTechId, toEmail, status)).thenReturn(emailNotificationDto);

        // WHEN
        emailService.sendEmail(notificationMapper.toEmailNotificationDto(travelTechId, toEmail, status));

        // THEN
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(notificationMapper, times(1)).toEmailNotificationDto(travelTechId, toEmail, status);
    }

    @Test
    void should_send_approval_notification_successfully() {
        // GIVEN
        String toEmail = "user@test.com";
        UUID travelTechId = UUID.randomUUID();
        TravelReportStatus status = TravelReportStatus.APPROVED;

        EmailNotificationDto emailNotificationDto = new EmailNotificationDto(
                toEmail,
                "Travel Expense Report APPROVED",
                "Dear User,\n\nYour travel expense report with ID " + travelTechId + " has been approved.\n\nBest regards,\nYour company");

        when(notificationMapper.toEmailNotificationDto(travelTechId, toEmail, status)).thenReturn(emailNotificationDto);

        // WHEN
        emailService.sendEmail(notificationMapper.toEmailNotificationDto(travelTechId, toEmail, status));

        // THEN
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(notificationMapper, times(1)).toEmailNotificationDto(travelTechId, toEmail, status);
    }

    @Test
    void should_throw_exception_when_sending_email_fails() {
        // GIVEN
        String toEmail = "user@test.com";
        UUID travelTechId = UUID.randomUUID();
        TravelReportStatus status = TravelReportStatus.REJECTED;

        EmailNotificationDto emailNotificationDto = new EmailNotificationDto(
                toEmail,
                "Travel Expense Report REJECTED",
                "Dear User,\n\nYour travel expense report with ID " + travelTechId + " has been rejected.\n\nBest regards,\nYour company");

        when(notificationMapper.toEmailNotificationDto(travelTechId, toEmail, status)).thenReturn(emailNotificationDto);

        doThrow(new RuntimeException("Email server error"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        // WHEN & THEN
        assertThatThrownBy(() -> emailService.sendEmail(notificationMapper.toEmailNotificationDto(travelTechId, toEmail, status)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email server error");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(notificationMapper, times(1)).toEmailNotificationDto(travelTechId, toEmail, status);
    }
}
