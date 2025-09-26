package org.notification_service.repository;

import org.notification_service.model.Notification;
import org.notification_service.model.NotificationStatus;
import org.notification_service.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByUserId(UUID userId);

    List<Notification> findByType(NotificationType type);

    List<Notification> findByStatus(NotificationStatus status);

}
