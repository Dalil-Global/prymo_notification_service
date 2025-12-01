package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notification_service.dto.event.SmsNotificationEvent;
import org.notification_service.model.NotificationEntity;
import org.notification_service.model.NotificationStatus;
import org.notification_service.repository.NotificationRepository;
import org.notification_service.template.SmsTemplateProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {
    private final RestTemplate restTemplate;
    private final NotificationRepository notificationRepository;
    private final SmsTemplateProcessor smsTemplateProcessor;
    private final DeliveryTrackingService deliveryTrackingService;

    private static final String TERMII_URL = "https://v3.api.termii.com";
    private static final String API_KEY = "YOUR_TERMII_API_KEY";

    public void processSmsEvent(SmsNotificationEvent event) {
        try {
            // 1. Load template
            String templatePath = "templates/sms/" + event.getTemplateName() + ".txt";
            String template = smsTemplateProcessor.loadTemplate(templatePath);
            String body = smsTemplateProcessor.render(template, event.getVariables());

            // 2. Save notification entry
            NotificationEntity notification = NotificationEntity.builder()
                    .recipientPhone(event.getRecipientPhone())
                    .body(body)
                    .type("SMS")
                    .channel(event.getChannel())
                    .status(NotificationStatus.PENDING)
                    .subject(event.getTemplateName())
                    .build();

            notification = notificationRepository.save(notification);
            deliveryTrackingService.incrementAttempts(notification);

            // 3. Call Termii
            Map<String, Object> payload = new HashMap<>();
            payload.put("api_key", API_KEY);
            payload.put("to", event.getRecipientPhone());
            payload.put("from", "PRYMO_APP");
            payload.put("sms", body);
            payload.put("type", "plain");
            payload.put("channel", event.getChannel());

            restTemplate.postForEntity(TERMII_URL, payload, String.class);

            // 4. Success
            deliveryTrackingService.markSent(notification);

        } catch (Exception ex) {
            log.error("Failed to send SMS: {}", ex.getMessage());
            deliveryTrackingService.markFailed(null, ex.getMessage());
        }
    }
}
