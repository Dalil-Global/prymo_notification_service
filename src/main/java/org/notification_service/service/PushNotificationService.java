package org.notification_service.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendPushRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.model.PushToken;
import org.notification_service.repository.PushTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushTokenRepository pushTokenRepository;

    public NotificationResponse sendPush(SendPushRequest request) {
        try {
            // Build FCM notification
            Notification notification = Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .build();

            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(request.getDeviceToken())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            return NotificationResponse.builder()
                    .id(null)
                    .status("SENT")
                    .recipientEmail(null)
                    .subject(request.getTitle())
                    .build();

        } catch (Exception e) {
            return NotificationResponse.builder()
                    .id(null)
                    .status("FAILED")
                    .subject(request.getTitle())
                    .build();
        }
    }

    public void registerToken(PushToken token) {
        pushTokenRepository.findByDeviceToken(token.getDeviceToken())
                .ifPresentOrElse(
                        existing -> {
                            existing.setActive(true);
                            pushTokenRepository.save(existing);
                        },
                        () -> pushTokenRepository.save(token)
                );
    }
}

