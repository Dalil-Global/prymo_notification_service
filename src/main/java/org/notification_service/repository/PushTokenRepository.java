package org.notification_service.repository;

import org.notification_service.model.PushToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PushTokenRepository extends JpaRepository<PushToken, UUID> {
    Optional<PushToken> findByDeviceToken(String deviceToken);
}

