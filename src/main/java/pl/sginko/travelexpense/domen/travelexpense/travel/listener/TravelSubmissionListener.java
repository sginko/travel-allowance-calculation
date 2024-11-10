package pl.sginko.travelexpense.domen.travelexpense.travel.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.sginko.travelexpense.domen.emailService.EmailService;
import pl.sginko.travelexpense.domen.travelexpense.travel.event.TravelSubmissionEvent;

@Slf4j
@AllArgsConstructor
@Component
public class TravelSubmissionListener {
    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @EventListener
    public void handleTravelSubmissionEvent(TravelSubmissionEvent event) {
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
