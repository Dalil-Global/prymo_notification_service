package org.notification_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EmailResponse {
    private UUID notificationId;
    private String status;
    private String message;
}
