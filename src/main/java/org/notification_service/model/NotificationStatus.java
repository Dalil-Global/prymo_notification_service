package org.notification_service.model;

public enum NotificationStatus {
    PENDING,
    QUEUED, // ✅ Added this new status
    SENT,
    FAILED
}
