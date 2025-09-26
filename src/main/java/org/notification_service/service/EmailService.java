package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendEmailRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.model.Notification;
import org.notification_service.model.NotificationStatus;
import org.notification_service.repository.NotificationRepository;
import org.notification_service.template.EmailTemplateProcessor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private final EmailTemplateProcessor emailTemplateProcessor;

    public NotificationResponse sendEmail(SendEmailRequest request) {
        Notification notification = Notification.builder()
                .recipientEmail(request.getRecipientEmail())
                .subject(request.getSubject())
                .body(request.getBody())
                .status(NotificationStatus.PENDING)
                .build();

        notification = notificationRepository.save(notification);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(request.getRecipientEmail());
            helper.setSubject(request.getSubject());

            String content;

            if (request.getTemplatePath() != null && !request.getTemplatePath().isBlank()) {
                // Use template if provided
                Map<String, Object> variables = request.getVariables() != null ? request.getVariables() : Map.of();
                String rawTemplate = emailTemplateProcessor.loadTemplate(request.getTemplatePath());
                content = emailTemplateProcessor.render(rawTemplate, variables);
                helper.setText(content, true); // true = HTML
            } else {
                // fallback: plain text
                content = request.getBody();
                helper.setText(content, false);
            }

            mailSender.send(message);

            notification.setStatus(NotificationStatus.SENT);
            notification.setBody(content);

        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
        }

        notificationRepository.save(notification);

        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientEmail(notification.getRecipientEmail())
                .subject(notification.getSubject())
                .status(notification.getStatus().name())
                .build();
    }
}
