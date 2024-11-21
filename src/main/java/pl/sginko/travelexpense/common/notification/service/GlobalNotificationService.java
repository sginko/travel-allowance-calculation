package pl.sginko.travelexpense.common.notification.service;

import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

import java.util.UUID;

public interface GlobalNotificationService {

    void sendEmailNotification(UUID travelTechId, String userEmail, TravelReportStatus status);
}
