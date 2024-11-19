package pl.sginko.travelexpense.domain.approval.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.sginko.travelexpense.domain.approval.event.ApprovalEvent;
import pl.sginko.travelexpense.common.notification.NotificationService;

@Slf4j
@AllArgsConstructor
@Component
public class ApprovalListener {
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @EventListener
    public void handleApprovalEvent(ApprovalEvent event) {
        try {
            notificationService.sendApprovalNotification(
                    event.getUserEmail(),
                    event.getTravelTechId(),
                    event.getStatus()
            );
            log.info("Sent notification on email {} about status {}", event.getUserEmail(), event.getStatus());
        } catch (Exception e) {
            log.error("Error sent notification on email {}: {}", event.getUserEmail(), e.getMessage());
        }
    }
}
