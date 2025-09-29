package org.notification_service.dto.request;

import lombok.*;
import org.notification_service.model.NotificationStatus;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendEmailRequest {
    private String recipientEmail;
    private UUID userId;
    private String subject;
    private String templateName;
    private NotificationStatus status;

    private String body;
    private String templatePath; // e.g. "templates/email/welcome.html"
    private Map<String, Object> variables; // e.g. { "name": "John" }
}