package org.notification_service.model;

public enum DeliveryChannel {
    SMTP,     // email
    DND,      // transactional SMS
    GENERIC,  // promotional SMS
    FCM       // push notification
}
