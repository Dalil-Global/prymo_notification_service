package org.notification_service.dto.event;

import lombok.Data;
import java.util.Map;
import java.util.UUID;

@Data
public class SmsNotificationEvent {
    private UUID notificationId;
    private UUID userId;
    private String recipientPhone;
    private String templateName;
    private String channel; // dnd/generic
    private Map<String,Object> variables;
}

