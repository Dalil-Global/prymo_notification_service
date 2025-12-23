package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notification_service.dto.event.EmailNotificationEvent;
import org.notification_service.dto.request.SendEmailRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.event.publisher.NotificationEventPublisher;
import org.notification_service.model.NotificationEntity;
import org.notification_service.model.NotificationStatus;
import org.notification_service.model.NotificationPriority;
import org.notification_service.provider.email.EmailProvider;
import org.notification_service.provider.email.EmailSendResult;
import org.notification_service.repository.NotificationRepository;
import org.notification_service.template.EmailTemplateProcessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final NotificationRepository notificationRepository;
    private final EmailTemplateProcessor templateProcessor;
    private final DeliveryTrackingService trackingService;
    private final NotificationEventPublisher eventPublisher;
    private final EmailProvider emailProvider;

    public NotificationResponse sendEmail(SendEmailRequest request) {

        // 1. Render Template FIRST (Before saving to DB)
        String rawTemplate = templateProcessor.loadTemplate(request.getTemplatePath());
        String finalBody = templateProcessor.render(rawTemplate, request.getVariables());

        // 2. Build and Save Record
        NotificationEntity record = NotificationEntity.builder()
                .userId(request.getUserId())
                .recipientEmail(request.getRecipientEmail())
                .channel("EMAIL")
                .type(request.getTemplateName())
                .priority(NotificationPriority.NORMAL)
                .status(NotificationStatus.PENDING)
                .attempts(0)
                .body(finalBody) // ✅ Fix 1: Populated
                .message(request.getSubject()) // ✅ Fix 2: Populated (Using Subject as 'Message' summary)
                .subject(request.getSubject())
                .build();

        record = notificationRepository.save(record);

        // 3. Publish Event
        EmailNotificationEvent event = new EmailNotificationEvent();
        event.setUserId(record.getUserId());
        event.setRecipientEmail(record.getRecipientEmail());
        event.setSubject(request.getSubject());
        event.setTemplateName(request.getTemplateName());
        event.setVariables(request.getVariables());

        eventPublisher.publishEmail(event);
        trackingService.markQueued(record);

        return NotificationResponse.builder()
                .id(record.getId())
                .recipientEmail(record.getRecipientEmail())
                .subject(request.getSubject())
                .status(record.getStatus().name())
                .build();
    }

    // Processing method (Listener calls this)
    public void processEmailDelivery(EmailNotificationEvent event) {
        log.info("Processing email delivery for user: {}", event.getUserId());

        SendEmailRequest providerRequest = SendEmailRequest.builder()
                .recipientEmail(event.getRecipientEmail())
                .subject(event.getSubject())
                .body("Rendered Content Placeholder")
                .build();

        EmailSendResult result = emailProvider.sendEmail(providerRequest);

        if (result.isSuccess()) {
            log.info("Email sent successfully to {}", event.getRecipientEmail());
        } else {
            log.error("Failed to send email: {}", result.getMessage());
        }
    }

    // Direct send (Synchronous)
    public void send(SendEmailRequest request) {
        log.info("Directly sending email (Synchronous) to {}", request.getRecipientEmail());
        emailProvider.sendEmail(request);
    }
}