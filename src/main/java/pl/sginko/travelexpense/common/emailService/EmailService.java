package pl.sginko.travelexpense.common.emailService;

import pl.sginko.travelexpense.common.notification.dto.EmailNotificationDto;

public interface EmailService {

    void sendEmail(EmailNotificationDto notificationDto);
}
