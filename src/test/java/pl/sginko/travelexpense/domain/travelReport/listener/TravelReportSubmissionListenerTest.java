package pl.sginko.travelexpense.domain.travelReport.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.common.notification.NotificationService;
import pl.sginko.travelexpense.domain.travelReport.event.TravelReportSubmissionEvent;
import pl.sginko.travelexpense.domain.travelReport.listener.TravelReportSubmissionListener;

import java.util.UUID;

import static org.mockito.Mockito.*;

class TravelReportSubmissionListenerTest {
    @Mock
    private NotificationService notificationService;

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
        verify(notificationService, times(1)).sendSubmissionNotification(userEmail, travelTechId);
    }

    @Test
    void should_log_error_when_email_service_throws_exception() {
        // GIVEN
        UUID travelTechId = UUID.randomUUID();
        String userEmail = "user@example.com";
        TravelReportSubmissionEvent event = new TravelReportSubmissionEvent(travelTechId, userEmail);

        // Имитируем исключение при вызове notification
        doThrow(new RuntimeException("Email service failed")).when(notificationService).sendSubmissionNotification(userEmail, travelTechId);

        // WHEN
        travelSubmissionListener.handleTravelSubmissionEvent(event);

        // THEN
        verify(notificationService, times(1)).sendSubmissionNotification(userEmail, travelTechId);
    }
}