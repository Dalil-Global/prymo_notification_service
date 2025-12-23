package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notification_service.dto.event.SmsNotificationEvent;
import org.notification_service.dto.request.SendSmsRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.event.publisher.NotificationEventPublisher;
import org.notification_service.model.NotificationEntity;
import org.notification_service.model.NotificationStatus;
import org.notification_service.provider.sms.SmsProvider;
import org.notification_service.provider.sms.SmsSendResult;
import org.notification_service.repository.NotificationRepository;
import org.notification_service.template.SmsTemplateProcessor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    private final NotificationRepository notificationRepository;
    private final SmsTemplateProcessor smsTemplateProcessor;
    private final DeliveryTrackingService deliveryTrackingService;
    private final NotificationEventPublisher eventPublisher;
    private final SmsProvider smsProvider;

    public NotificationResponse sendSms(SendSmsRequest request) {

        // 1. Render Template
        String body = request.getMessage();
        if (request.getTemplateName() != null) {
            String rawTemplate = smsTemplateProcessor.loadTemplate("templates/sms/" + request.getTemplateName() + ".txt");
            body = smsTemplateProcessor.render(rawTemplate, request.getVariables());
        }

        // 2. Save Notification
        NotificationEntity notification = NotificationEntity.builder()
                .userId(request.getUserId())
                .recipientPhone(request.getRecipientPhone())
                .recipientEmail("N/A") // Needed if DB constraint exists, otherwise optional
                .body(body)
                .message(body) // ✅ Fix: Populate 'message' field
                .type("SMS")
                .channel(request.getChannel() != null ? request.getChannel() : "dnd")
                .status(NotificationStatus.PENDING)
                .subject(request.getTemplateName())
                .attempts(0)
                .build();

        notification = notificationRepository.save(notification);

        // 3. Publish Event
        SmsNotificationEvent event = new SmsNotificationEvent();
        event.setNotificationId(notification.getId());
        event.setUserId(request.getUserId());
        event.setRecipientPhone(request.getRecipientPhone());
        event.setTemplateName(request.getTemplateName());
        event.setChannel(notification.getChannel());
        event.setVariables(request.getVariables());

        eventPublisher.publishSms(event);
        deliveryTrackingService.markQueued(notification);

        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientPhone(notification.getRecipientPhone())
                .message(body)
                .status(NotificationStatus.QUEUED.name())
                .build();
    }

    public void processSmsDelivery(SmsNotificationEvent event) {
        log.info("Processing SMS Delivery for ID: {}", event.getNotificationId());

        Optional<NotificationEntity> notificationOpt = notificationRepository.findById(event.getNotificationId());
        if (notificationOpt.isEmpty()) {
            log.error("Notification record {} not found. Aborting.", event.getNotificationId());
            return;
        }

        NotificationEntity notification = notificationOpt.get();

        if (notification.getStatus() == NotificationStatus.SENT) {
            log.warn("Notification {} is already SENT. Skipping.", notification.getId());
            return;
        }

        try {
            deliveryTrackingService.incrementAttempts(notification);
            SmsSendResult result = smsProvider.sendSms(
                    notification.getRecipientPhone(),
                    notification.getBody(),
                    notification.getId().toString()
            );

            if (result.isSuccess()) {
                notification.setProviderMessageId(result.getProviderResponse());
                deliveryTrackingService.markSent(notification);
                log.info("SMS {} sent successfully.", notification.getId());
            } else {
                deliveryTrackingService.markFailed(notification, result.getProviderResponse());
                log.error("SMS {} failed at provider: {}", notification.getId(), result.getProviderResponse());
            }

        } catch (Exception ex) {
            log.error("Unexpected error sending SMS {}: {}", notification.getId(), ex.getMessage());
            deliveryTrackingService.markFailed(notification, "Internal Error: " + ex.getMessage());
        }
    }
}