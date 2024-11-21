package pl.sginko.travelexpense.domain.travelReport.notification;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.common.emailService.EmailService;
import pl.sginko.travelexpense.common.notification.dto.EmailNotificationDto;
import pl.sginko.travelexpense.common.notification.mapper.NotificationMapper;
import pl.sginko.travelexpense.common.notification.service.GlobalNotificationService;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.domain.travelReport.event.TravelReportSubmissionEvent;

import java.util.UUID;

@AllArgsConstructor
@Service
public class TravelReportNotificationServiceImpl implements TravelReportNotificationService, GlobalNotificationService {
    private final EmailService emailService;
    private final NotificationMapper notificationMapper;

    @EventListener
    @Override
    public void handleTravelReportSubmissionEvent(TravelReportSubmissionEvent event) {
        sendEmailNotification(event.getTravelTechId(), event.getUserEmail(), event.getStatus());
    }

    @Override
    public void sendEmailNotification(UUID travelTechId, String userEmail, TravelReportStatus status) {
        EmailNotificationDto emailNotificationDto = notificationMapper.toEmailNotificationDto(travelTechId, userEmail, status);
        emailService.sendEmail(emailNotificationDto);
    }
}
