package org.notification_service.dto.request;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@Builder
public class SendSmsRequest {
    private UUID userId;
    private String recipientPhone;   // E.164 or local format depending on Termii setup
    private String message;          // SMS body
    private String externalId;       // optional: business reference
    private String channel;
    private String templateName;
    private Long senderId;
    private Map<String, Object> variables;
}
