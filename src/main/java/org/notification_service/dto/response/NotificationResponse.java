package org.notification_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationResponse {

    private UUID id;
    private String recipientEmail;
    private String recipientPhone;
    private String subject;
    private String message;
    private String channel;
    private String type;
    private String status;
    private String priority;
    private String providerMessageId;

    // ✅ New fields for tracking
    private LocalDateTime sentAt;
    private String errorMessage;
    private Integer attempts;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
