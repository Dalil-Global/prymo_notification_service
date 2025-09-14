package org.notification_service.repository;

import org.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByStatusAndAttemptsLessThan(String status, Integer maxAttempts);

}