package org.notification_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notification_service.model.NotificationEntity;
import org.notification_service.model.NotificationStatus;
import org.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DeliveryTrackingService {

    private final NotificationRepository notificationRepository;

    public void markSent(NotificationEntity notification) {
        // ✅ FIX: Removed the buggy line: notification.setSentAt(LocalDateTime.parse("SENT"));

        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        notification.setErrorMessage(null);
        notificationRepository.save(notification);
        log.debug("Notification {} marked as SENT", notification.getId());
    }

    public void markFailed(NotificationEntity notification, String error) {
        if (notification == null) return;
        notification.setStatus(NotificationStatus.FAILED);
        notification.setErrorMessage(error);
        notificationRepository.save(notification);
        log.error("Notification {} marked as FAILED: {}", notification.getId(), error);
    }

    public void incrementAttempts(NotificationEntity notification) {
        notification.setAttempts(notification.getAttempts() == null ? 1 : notification.getAttempts() + 1);
        notificationRepository.save(notification);
    }

    // ✅ FIX: Added the missing method
    public void markQueued(NotificationEntity notification) {
        notification.setStatus(NotificationStatus.QUEUED);
        notificationRepository.save(notification);
        log.info("Notification {} marked as QUEUED", notification.getId());
    }
}