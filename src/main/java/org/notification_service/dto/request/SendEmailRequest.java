package org.notification_service.dto.request;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendEmailRequest {
    private String recipientEmail;
    private String subject;
    private String body;
    private String templatePath; // e.g. "templates/email/welcome.html"
    private Map<String, Object> variables; // e.g. { "name": "John" }
}