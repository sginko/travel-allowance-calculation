//package pl.sginko.travelexpense.common.notification.service;
//
//import lombok.AllArgsConstructor;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import pl.sginko.travelexpense.common.emailService.EmailService;
//import pl.sginko.travelexpense.common.notification.dto.EmailNotificationDto;
//import pl.sginko.travelexpense.common.notification.mapper.NotificationMapper;
//import pl.sginko.travelexpense.domain.approval.event.TravelReportApprovalEvent;
//import pl.sginko.travelexpense.domain.travelReport.event.TravelReportSubmissionEvent;
//
//@AllArgsConstructor
//@Service
//public class NotificationServiceImpl {
//    private final EmailService emailService;
//    private final NotificationMapper eventMapper;
//
//    @Async
//    @EventListener
//    public void handleTravelReportSubmissionEvent(TravelReportSubmissionEvent event) {
//        EmailNotificationDto notification = eventMapper.toEmailNotificationDto(
//                event.getTravelTechId(),
//                event.getUserEmail(),
//                event.getStatus());
//        emailService.sendEmail(notification);
//    }
//
//    @Async
//    @EventListener
//    public void handleTravelReportApprovalEvent(TravelReportApprovalEvent event) {
//        EmailNotificationDto notification = eventMapper.toEmailNotificationDto(
//                event.getTravelTechId(),
//                event.getUserEmail(),
//                event.getStatus());
//        emailService.sendEmail(notification);
//    }
//
////    @Override
////    public void sendGeneralNotification(String email, String subject, String body) {
////        EmailNotificationDto notification = new EmailNotificationDto(email, subject, body);
////        emailService.sendEmail(notification);
////    }
//}
