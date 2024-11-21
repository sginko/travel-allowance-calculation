package pl.sginko.travelexpense.common.emailService;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.common.notification.dto.EmailNotificationDto;

@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailNotificationDto notificationDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ksiegowosc@ginkota.pl");
        message.setTo(notificationDto.getToEmail());
        message.setSubject(notificationDto.getSubject());
        message.setText(notificationDto.getBody());
        mailSender.send(message);
    }
}
