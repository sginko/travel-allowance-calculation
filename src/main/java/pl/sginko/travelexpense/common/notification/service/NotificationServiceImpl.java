//package pl.sginko.travelexpense.common.listener.service;
//
//import lombok.AllArgsConstructor;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import pl.sginko.travelexpense.common.emailService.EmailService;
//import pl.sginko.travelexpense.common.listener.dto.EmailNotificationDto;
//import pl.sginko.travelexpense.common.listener.mapper.NotificationMapper;
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
//        EmailNotificationDto listener = eventMapper.toEmailNotificationDto(
//                event.getTravelTechId(),
//                event.getUserEmail(),
//                event.getStatus());
//        emailService.sendEmail(listener);
//    }
//
//    @Async
//    @EventListener
//    public void handleTravelReportApprovalEvent(TravelReportApprovalEvent event) {
//        EmailNotificationDto listener = eventMapper.toEmailNotificationDto(
//                event.getTravelTechId(),
//                event.getUserEmail(),
//                event.getStatus());
//        emailService.sendEmail(listener);
//    }
//
////    @Override
////    public void sendGeneralNotification(String email, String subject, String body) {
////        EmailNotificationDto listener = new EmailNotificationDto(email, subject, body);
////        emailService.sendEmail(listener);
////    }
//}
