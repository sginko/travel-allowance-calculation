package pl.sginko.travelexpense.common.notification;

import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

import java.util.UUID;

public interface NotificationService {

    void sendSubmissionNotification(String toEmail, UUID travelTechId);

    void sendApprovalNotification(String toEmail, UUID travelTechId, TravelReportStatus status);
}
