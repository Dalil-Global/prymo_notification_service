package org.notification_service.provider.sms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsSendResult {
    private boolean success;
    private String providerResponse;
}
