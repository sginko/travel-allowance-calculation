package pl.sginko.travelexpense.domen.emailService;

import pl.sginko.travelexpense.domen.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelStatus;

import java.util.UUID;

public interface EmailService {

//    void sendApprovalNotification(String toEmail, UUID travelTechId, ApprovalStatus status);

    void sendSubmissionNotification(String toEmail, UUID travelTechId);
    void sendApprovalNotification(String toEmail, UUID travelTechId, TravelStatus status);
}
