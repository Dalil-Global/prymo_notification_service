package org.notification_service.repository;

import org.notification_service.model.NotificationEntity;
import org.notification_service.model.NotificationStatus;
import org.notification_service.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    List<NotificationEntity> findByUserId(UUID userId);

    List<NotificationEntity> findByType(NotificationType type);

    List<NotificationEntity> findByStatus(NotificationStatus status);

    List<NotificationEntity>findByStatusAndAttemptsLessThan (NotificationStatus status, int attempts);

}
