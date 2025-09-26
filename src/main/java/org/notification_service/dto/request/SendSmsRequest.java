package org.notification_service.dto.request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@Builder
public class SendSmsRequest {
    private String recipientPhone;   // E.164 or local format depending on Termii setup
    private String message;          // SMS body
    private String externalId;       // optional: business reference
}
