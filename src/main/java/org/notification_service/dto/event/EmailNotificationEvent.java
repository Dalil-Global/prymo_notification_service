package org.notification_service.dto.event;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class EmailNotificationEvent {
    private UUID userId;
    private String recipientEmail;
    private String subject;
    private String templateName;
    private Map<String,Object> variables;
}
