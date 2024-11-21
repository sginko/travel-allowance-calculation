package pl.sginko.travelexpense.domain.approval.notification;

import pl.sginko.travelexpense.domain.approval.event.TravelReportApprovalEvent;

public interface ApprovalNotificationService {

    void handleApprovalEvent(TravelReportApprovalEvent event);
}
