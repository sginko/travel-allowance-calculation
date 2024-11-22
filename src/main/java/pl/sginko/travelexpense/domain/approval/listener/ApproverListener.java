package pl.sginko.travelexpense.domain.approval.listener;

import pl.sginko.travelexpense.domain.approval.event.TravelReportApprovalEvent;

public interface ApproverListener {

    void handleApprovalEvent(TravelReportApprovalEvent event);
}
