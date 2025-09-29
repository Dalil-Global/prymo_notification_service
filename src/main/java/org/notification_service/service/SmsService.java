package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendSmsRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.model.Notification;
import org.notification_service.model.NotificationStatus;
import org.notification_service.repository.NotificationRepository;
import org.notification_service.template.SmsTemplateProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final RestTemplate restTemplate;
    private final NotificationRepository notificationRepository;
    private final SmsTemplateProcessor smsTemplateProcessor;
    private final DeliveryTrackingService deliveryTrackingService;

    private static final String TERMII_URL = "https://v3.api.termii.com";
    private static final String API_KEY = "YOUR_TERMII_API_KEY"; // move to application.properties

    public NotificationResponse sendSms(SendSmsRequest request) {
        Map<String, Object> variables = new HashMap<>();
        if (request.getVariables() != null) {
            variables.putAll(request.getVariables());
        }

        // Load & render template
        String templatePath = STR."src/main/resources/templates/sms/\{request.getTemplateName()}.txt";
        String templateContent = smsTemplateProcessor.loadTemplate(templatePath);
        String renderedContent = smsTemplateProcessor.render(templateContent, variables);

        // Save notification entry
        Notification notification = Notification.builder()
                .recipientPhone(request.getRecipientPhone())
                .subject(request.getTemplateName())
                .body(renderedContent)
                .status(NotificationStatus.PENDING)
                .channel(request.getChannel())
                .type("SMS")
                .build();

        notification = notificationRepository.save(notification);
        deliveryTrackingService.incrementAttempts(notification);

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("api_key", API_KEY);
            payload.put("to", request.getRecipientPhone());
            payload.put("from", request.getSenderId());
            payload.put("sms", renderedContent);
            payload.put("type", "plain");
            payload.put("channel", request.getChannel());

            restTemplate.postForEntity(TERMII_URL, payload, String.class);

            deliveryTrackingService.markSent(notification);
        } catch (Exception e) {
            deliveryTrackingService.markFailed(notification, e.getMessage());
        }

        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientPhone(notification.getRecipientPhone())
                .subject(notification.getSubject())
                .status(notification.getStatus().name())
                .sentAt(notification.getSentAt())
                .errorMessage(notification.getErrorMessage())
                .attempts(notification.getAttempts())
                .channel(notification.getChannel())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}
