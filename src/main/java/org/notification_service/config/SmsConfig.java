package org.notification_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Value("${termii.api.base-url}")
    private String baseUrl;

    @Value("${termii.api.key}")
    private String apiKey;

    @Value("${termii.sender.id:PRYMO}")
    private String senderId;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSenderId() {
        return senderId;
    }
}

