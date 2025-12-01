package org.notification_service.service;

import jakarta.transaction.Transactional;
import org.notification_service.model.NotificationEntity;
import org.notification_service.model.NotificationStatus;
import org.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class DeliveryTrackingService {

    private final NotificationRepository notificationRepository;

    public DeliveryTrackingService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void markSent(NotificationEntity notification) {
        notification.setSentAt(LocalDateTime.parse("SENT"));
        notification.setSentAt(LocalDateTime.now());
        notification.setErrorMessage(null);
        notificationRepository.save(notification);
    }

    public void markFailed(NotificationEntity notification, String error) {
        notification.setStatus(NotificationStatus.valueOf("FAILED"));
        notification.setErrorMessage(error);
        notificationRepository.save(notification);
    }

    public void incrementAttempts(NotificationEntity notification) {
        notification.setAttempts(notification.getAttempts() == null ? 1 : notification.getAttempts() + 1);
        notificationRepository.save(notification);
    }
}
