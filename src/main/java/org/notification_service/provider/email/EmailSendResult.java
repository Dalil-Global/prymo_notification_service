package org.notification_service.provider.email;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailSendResult {
    private boolean success;
    private String message;          // human-readable message
    private String errorDetails;     // exception details if any
}
