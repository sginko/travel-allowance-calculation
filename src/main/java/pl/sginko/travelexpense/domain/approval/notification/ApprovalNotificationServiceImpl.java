package pl.sginko.travelexpense.domain.approval.notification;

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
public class ApprovalNotificationServiceImpl implements ApprovalNotificationService, GlobalNotificationService {
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
