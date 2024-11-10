package pl.sginko.travelexpense.domen.emailService;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelStatus;

import java.util.UUID;

@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendSubmissionNotification(String toEmail, UUID travelTechId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ksiegowosc@ginkota.pl");
        message.setTo(toEmail);
        message.setSubject("Travel Expense Report Submitted");
        message.setText(buildSubmissionEmailContent(travelTechId));
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendApprovalNotification(String toEmail, UUID travelTechId, TravelStatus status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ksiegowosc@ginkota.pl");
        message.setTo(toEmail);
        message.setSubject("Travel Expense Report " + status.name());
        message.setText(buildApprovalEmailContent(travelTechId, status));
        mailSender.send(message);
    }

    private String buildSubmissionEmailContent(UUID travelTechId) {
        return "Dear User,\n\n" +
                "Your travel expense report with ID " + travelTechId + " has been submitted.\n" +
                "You can view the details in your account.\n\n" +
                "Best regards,\n" +
                "Ginkota";
    }

    private String buildApprovalEmailContent(UUID travelTechId, TravelStatus status) {
        return "Dear User,\n\n" +
                "Your travel expense report with ID " + travelTechId + " has been " +
                status.name().toLowerCase() + ".\n" +
                "You can view the details in your account.\n\n" +
                "Best regards,\n" +
                "Ginkota";
    }
}
