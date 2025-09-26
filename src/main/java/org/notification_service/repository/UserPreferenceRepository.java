package org.notification_service.repository;

import org.notification_service.model.UserNotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserPreferenceRepository extends JpaRepository<UserNotificationPreference, UUID> {
    Optional<UserNotificationPreference> findByUserId(UUID userId);
}
