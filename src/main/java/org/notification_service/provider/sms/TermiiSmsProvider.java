package org.notification_service.provider.sms;

import org.notification_service.config.SmsConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class TermiiSmsProvider implements SmsProvider {

    private final WebClient webClient;
    private final SmsConfig smsConfig;

    public TermiiSmsProvider(SmsConfig smsConfig) {
        this.smsConfig = smsConfig;
        this.webClient = WebClient.builder()
                .baseUrl(smsConfig.getBaseUrl())
                .build();
    }

    @Override
    public SmsSendResult sendSms(String to, String message, String externalId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("api_key", smsConfig.getApiKey());
        payload.put("to", to); // Must be in international format e.g., 234xxxxxxxxxx
        payload.put("from", smsConfig.getSenderId());
        payload.put("sms", message);
        payload.put("type", "plain"); // plain | unicode | encrypted
        payload.put("channel", "dnd"); // default to transactional

        try {
            Map<String, Object> resp = webClient.post()
                    .uri("/api/sms/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            boolean success = resp != null && "ok".equalsIgnoreCase(String.valueOf(resp.get("code")));
            String providerResponse = resp == null ? "no-response" : resp.toString();

            return new SmsSendResult(success, providerResponse);
        } catch (Exception ex) {
            return new SmsSendResult(false, ex.getMessage());
        }
    }
}