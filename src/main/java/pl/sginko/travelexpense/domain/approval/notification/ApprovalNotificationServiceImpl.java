//package pl.sginko.travelexpense.domain.approval.listener;
//
//import lombok.AllArgsConstructor;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//import pl.sginko.travelexpense.common.emailService.EmailService;
//import pl.sginko.travelexpense.common.listener.dto.EmailNotificationDto;
//import pl.sginko.travelexpense.common.listener.mapper.NotificationMapper;
//import pl.sginko.travelexpense.common.listener.service.GlobalNotificationService;
//import pl.sginko.travelexpense.domain.approval.event.TravelReportApprovalEvent;
//import pl.sginko.travelexpense.domain.approval.listener.ApproverListener;
//import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;
//
//import java.util.UUID;
//
//@AllArgsConstructor
//@Service
//public class ApprovalNotificationServiceImpl implements ApprovalNotificationService, ApproverListener, GlobalNotificationService {
//    private final EmailService emailService;
//    private final NotificationMapper notificationMapper;
//
//    @EventListener
//    @Override
//    public void handleApprovalEvent(TravelReportApprovalEvent event) {
//        sendEmailNotification(event.getTravelTechId(), event.getUserEmail(), event.getStatus());
//    }
//
//    @Override
//    public void sendEmailNotification(UUID travelTechId, String userEmail, TravelReportStatus status) {
//        EmailNotificationDto emailNotificationDto = notificationMapper.toEmailNotificationDto(travelTechId, userEmail, status);
//        emailService.sendEmail(emailNotificationDto);
//    }
//}
