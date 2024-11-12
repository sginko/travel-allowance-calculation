package pl.sginko.travelexpense.logic.approval.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import pl.sginko.travelexpense.logic.approval.event.ApprovalEvent;
import pl.sginko.travelexpense.logic.emailService.EmailService;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelStatus;

import java.util.UUID;

import static org.mockito.Mockito.*;

class ApprovalListenerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ApprovalListener approvalListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_handle_approval_event_and_send_email_successfully() {
        // GIVEN
        ApprovalEvent event = new ApprovalEvent(UUID.randomUUID(), "user@example.com", TravelStatus.APPROVED);

        // WHEN
        approvalListener.handleApprovalEvent(event);

        // THEN
        verify(emailService, times(1)).sendApprovalNotification(event.getUserEmail(), event.getTravelTechId(), event.getStatus());
    }

    @Test
    void should_handle_approval_event_and_log_error_when_email_fails() {
        // GIVEN
        ApprovalEvent event = new ApprovalEvent(UUID.randomUUID(), "user@example.com", TravelStatus.APPROVED);
        doThrow(new RuntimeException("Email server down"))
                .when(emailService).sendApprovalNotification(event.getUserEmail(), event.getTravelTechId(), event.getStatus());

        // WHEN
        approvalListener.handleApprovalEvent(event);

        // THEN
        verify(emailService, times(1)).sendApprovalNotification(event.getUserEmail(), event.getTravelTechId(), event.getStatus());
    }
}
