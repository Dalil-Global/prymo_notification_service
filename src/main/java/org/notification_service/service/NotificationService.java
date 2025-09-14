package org.notification_service.service;

import jakarta.transaction.Transactional;
import org.notification_service.dto.request.SendEmailRequest;
import org.notification_service.model.Notification;
import org.notification_service.model.NotificationTemplate;
import org.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TemplateService templateService;
    private final EmailService emailService;
    private final DeliveryTrackingService trackingService;

    public NotificationService(NotificationRepository notificationRepository,
                               TemplateService templateService,
                               EmailService emailService,
                               DeliveryTrackingService trackingService) {
        this.notificationRepository = notificationRepository;
        this.templateService = templateService;
        this.emailService = emailService;
        this.trackingService = trackingService;
    }

    /**
     * Saves a notification record, renders template, attempts send, records result.
     */
    public Notification sendEmail(UUID userId,
                                  String to,
                                  String subject,
                                  String templateName,
                                  Map<String, Object> variables) {

        Notification notification = Notification.builder()
                .userId(userId)
                .channel("EMAIL")
                .type(templateName)
                .status("PENDING")
                .priority("NORMAL")
                .attempts(0)
                .provider("SMTP")
                .recipientEmail(to)   // ✅ now stored
                .build();

        notification = notificationRepository.save(notification);

        NotificationTemplate template = templateService.findByName(templateName)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateName));

        String body = templateService.render(template.getContent(), variables);
        notification.setContent(body);
        notificationRepository.save(notification);

        try {
            SendEmailRequest req = new SendEmailRequest();
            req.setUserId(userId);
            req.setTo(to);
            req.setSubject(subject);
            req.setTemplateName(templateName);
            req.setVariables(variables);
            req.setBody(body);

            trackingService.incrementAttempts(notification);

            emailService.send(req);
            trackingService.markSent(notification);
        } catch (Exception e) {
            trackingService.markFailed(notification, e.getMessage());
        }

        return notification;
    }

}
