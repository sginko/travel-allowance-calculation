package pl.sginko.travelexpense.logic.emailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void should_send_submission_notification_successfully() {
        // GIVEN
        String toEmail = "user@test.com";
        UUID travelTechId = UUID.randomUUID();

        // WHEN
        emailService.sendSubmissionNotification(toEmail, travelTechId);

        // THEN
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void should_send_approval_notification_successfully() {
        // GIVEN
        String toEmail = "user@test.com";
        UUID travelTechId = UUID.randomUUID();
        TravelReportStatus status = TravelReportStatus.APPROVED;

        // WHEN
        emailService.sendApprovalNotification(toEmail, travelTechId, status);

        // THEN
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void should_throw_exception_when_sending_email_fails() {
        // GIVEN
        String toEmail = "user@test.com";
        UUID travelTechId = UUID.randomUUID();
        TravelReportStatus status = TravelReportStatus.APPROVED;

        doThrow(new RuntimeException("Email server error"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        // WHEN & THEN
        assertThatThrownBy(() -> emailService.sendApprovalNotification(toEmail, travelTechId, status))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email server error");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
