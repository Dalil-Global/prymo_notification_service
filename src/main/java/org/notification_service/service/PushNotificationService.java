package org.notification_service.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notification_service.dto.event.PushNotificationEvent;
import org.notification_service.model.NotificationStatus;
import org.notification_service.model.NotificationEntity;
import org.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

    private final NotificationRepository notificationRepository;

    public void processPushEvent(PushNotificationEvent event) {
        try {
            // 1. Save notification entry
            NotificationEntity record = NotificationEntity.builder()
                    .recipientPhone(null)
                    .recipientEmail(null)
                    .subject(event.getTitle())
                    .body(event.getBody())
                    .status(NotificationStatus.PENDING)
                    .type("PUSH")
                    .channel("PUSH")
                    .build();

            record = notificationRepository.save(record);

            // 2. Build firebase message
            Notification notification = Notification.builder()
                    .setTitle(event.getTitle())
                    .setBody(event.getBody())
                    .build();

            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(event.getDeviceToken())
                    .build();

            FirebaseMessaging.getInstance().send(message);

            // 3. Success
            record.setStatus(NotificationStatus.SENT);
            notificationRepository.save(record);

        } catch (Exception ex) {
            log.error("Failed to send push notification: {}", ex.getMessage());
        }
    }
}
