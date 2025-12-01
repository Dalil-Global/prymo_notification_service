package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendEmailRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.kafka.NotificationEvent;
import org.notification_service.kafka.NotificationEventProducer;
import org.notification_service.model.NotificationEntity;
import org.notification_service.model.NotificationStatus;
import org.notification_service.model.NotificationPriority;
import org.notification_service.repository.NotificationRepository;
import org.notification_service.template.EmailTemplateProcessor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final NotificationRepository notificationRepository;
    private final EmailTemplateProcessor templateProcessor;
    private final DeliveryTrackingService trackingService;
    private final NotificationEventProducer eventProducer;

    public NotificationResponse sendEmail(SendEmailRequest request) {

        // Save record
        NotificationEntity record = NotificationEntity.builder()
                .userId(request.getUserId())
                .recipientEmail(request.getRecipientEmail())
                .channel("EMAIL")
                .type(request.getTemplateName())
                .priority(NotificationPriority.NORMAL)
                .status(NotificationStatus.PENDING)
                .attempts(0)
                .build();

        record = notificationRepository.save(record);

        // Load + render template
        String rawTemplate = templateProcessor.loadTemplate(request.getTemplatePath());
        String finalBody = templateProcessor.render(rawTemplate, request.getVariables());
        record.setBody(finalBody);

        notificationRepository.save(record);

        trackingService.incrementAttempts(record);

        // Publish Kafka Event
        NotificationEvent event = NotificationEvent.builder()
                .notificationId(record.getId())
                .userId(record.getUserId())
                .channel("EMAIL")
                .recipient(record.getRecipientEmail())
                .subject(request.getSubject())
                .body(finalBody)
                .variables(request.getVariables())
                .build();

        eventProducer.publish("notifications.email.send", event);

        trackingService.markQueued(record);

        return NotificationResponse.builder()
                .id(record.getId())
                .recipientEmail(record.getRecipientEmail())
                .subject(request.getSubject())
                .status(record.getStatus().name())
                .build();
    }

    // Called internally by Kafka consumer later
    public void send(SendEmailRequest req) {
        // EMPTY — actual sending is moved to a Kafka consumer
    }
}
