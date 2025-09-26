package org.notification_service.provider.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendEmailRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpEmailProvider implements EmailProvider {

    private final JavaMailSender mailSender;

    @Override
    public EmailSendResult sendEmail(SendEmailRequest request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(request.getRecipientEmail());
            helper.setSubject(request.getSubject());
            helper.setText(request.getBody(), true); // true = HTML enabled

            mailSender.send(message);

            return new EmailSendResult(true, "Email sent successfully", null);
        } catch (MessagingException | jakarta.mail.MessagingException ex) {
            return new EmailSendResult(false, "Failed to send email", ex.getMessage());
        }
    }
}

