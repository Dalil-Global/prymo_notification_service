package org.notification_service.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notification_service.dto.event.PushNotificationEvent;
import org.notification_service.dto.request.SendPushRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.event.publisher.NotificationEventPublisher;
import org.notification_service.model.NotificationEntity;
import org.notification_service.model.NotificationStatus;
import org.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationEventPublisher eventPublisher;

    public NotificationResponse sendPush(SendPushRequest request) {

        NotificationEntity record = NotificationEntity.builder()
                .userId(request.getUserId())
                .recipientEmail("N/A") // Satisfy DB Non-Null constraint
                .subject(request.getTitle())
                .body(request.getBody())
                .message(request.getBody()) // ✅ Fix: Populate 'message' field
                .status(NotificationStatus.PENDING)
                .type("PUSH")
                .channel("PUSH")
                .build();

        record = notificationRepository.save(record);

        PushNotificationEvent event = new PushNotificationEvent();
        event.setUserId(request.getUserId());
        event.setDeviceToken(request.getDeviceToken());
        event.setTitle(request.getTitle());
        event.setBody(request.getBody());

        eventPublisher.publishPush(event);

        return NotificationResponse.builder()
                .id(record.getId())
                .status(record.getStatus().name())
                .build();
    }

    public void processPushDelivery(PushNotificationEvent event) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(event.getTitle())
                    .setBody(event.getBody())
                    .build();

            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(event.getDeviceToken())
                    .build();

            FirebaseMessaging.getInstance().send(message);
            log.info("Push notification sent to token: {}", event.getDeviceToken());

        } catch (Exception ex) {
            log.error("Failed to send push notification: {}", ex.getMessage());
        }
    }
}