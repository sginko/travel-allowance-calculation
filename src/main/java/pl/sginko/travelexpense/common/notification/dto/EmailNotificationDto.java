package pl.sginko.travelexpense.common.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailNotificationDto {
    private final String toEmail;
    private final String subject;
    private final String body;
}
