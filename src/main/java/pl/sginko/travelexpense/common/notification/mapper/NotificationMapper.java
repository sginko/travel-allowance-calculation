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
