package pl.sginko.travelexpense.logic.travelexpense.travelReport.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.logic.emailService.EmailService;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.event.TravelReportSubmissionEvent;

import java.util.UUID;

import static org.mockito.Mockito.*;

class TravelReportSubmissionListenerTest {
    @Mock
    private EmailService emailService;

    @InjectMocks
    private TravelReportSubmissionListener travelSubmissionListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_send_email_notification_when_event_is_handled() {
        // GIVEN
        UUID travelTechId = UUID.randomUUID();
        String userEmail = "user@example.com";
        TravelReportSubmissionEvent event = new TravelReportSubmissionEvent(travelTechId, userEmail);

        // WHEN
        travelSubmissionListener.handleTravelSubmissionEvent(event);

        // THEN
        verify(emailService, times(1)).sendSubmissionNotification(userEmail, travelTechId);
    }

    @Test
    void should_log_error_when_email_service_throws_exception() {
        // GIVEN
        UUID travelTechId = UUID.randomUUID();
        String userEmail = "user@example.com";
        TravelReportSubmissionEvent event = new TravelReportSubmissionEvent(travelTechId, userEmail);

        // Имитируем исключение при вызове emailService
        doThrow(new RuntimeException("Email service failed")).when(emailService).sendSubmissionNotification(userEmail, travelTechId);

        // WHEN
        travelSubmissionListener.handleTravelSubmissionEvent(event);

        // THEN
        verify(emailService, times(1)).sendSubmissionNotification(userEmail, travelTechId);
    }
}