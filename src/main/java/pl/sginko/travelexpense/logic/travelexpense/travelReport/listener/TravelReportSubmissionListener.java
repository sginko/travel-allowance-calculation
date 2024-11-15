package pl.sginko.travelexpense.logic.travelexpense.travelReport.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.sginko.travelexpense.logic.emailService.EmailService;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.event.TravelReportSubmissionEvent;

@Slf4j
@AllArgsConstructor
@Component
public class TravelReportSubmissionListener {
    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @EventListener
    public void handleTravelSubmissionEvent(TravelReportSubmissionEvent event) {
        try {
            emailService.sendSubmissionNotification(
                    event.getUserEmail(),
                    event.getTravelTechId()
            );
            log.info("Sent notification on email {} about report {}", event.getUserEmail(), event.getTravelTechId());
        } catch (Exception e) {
            log.error("Error sent notification on email {}: {}", event.getUserEmail(), e.getMessage());
        }
    }
}