package org.notification_service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendEmailRequest {
    private String recipientEmail;
    private String subject;
    private String body;
}