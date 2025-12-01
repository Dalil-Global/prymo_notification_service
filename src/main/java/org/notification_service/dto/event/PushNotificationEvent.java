package org.notification_service.dto.event;

import lombok.Data;
import java.util.Map;
import java.util.UUID;

@Data
public class PushNotificationEvent {
    private UUID userId;
    private String deviceToken;
    private String title;
    private String body;
    private String templateName; // optional
    private Map<String,Object> variables;
}

