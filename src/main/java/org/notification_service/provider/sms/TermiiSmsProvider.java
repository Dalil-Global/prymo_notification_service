package org.notification_service.provider.sms;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.notification_service.config.SmsConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
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
    @CircuitBreaker(name = "termii", fallbackMethod = "sendSmsFallback") // ✅ Protect this method
    public SmsSendResult sendSms(String to, String message, String externalId) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("api_key", smsConfig.getApiKey());
        payload.put("to", to);
        payload.put("from", smsConfig.getSenderId());
        payload.put("sms", message);
        payload.put("type", "plain");
        payload.put("channel", "dnd");

        try {
            Map<String, Object> resp = webClient.post()
                    .uri("/api/sms/send") // ✅ URL is now correct relative to base URL
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            boolean success = resp != null && (resp.containsKey("message_id") || "ok".equalsIgnoreCase(String.valueOf(resp.get("code"))));
            return new SmsSendResult(success, resp != null ? resp.toString() : "No response");

        } catch (Exception ex) {
            // Throwing exception triggers the Circuit Breaker to count it as a failure
            throw new RuntimeException("Termii API Failed: " + ex.getMessage());
        }
    }

    // ✅ Fallback method (Called when Circuit is Open or Exception occurs)
    public SmsSendResult sendSmsFallback(String to, String message, String externalId, Throwable t) {
        log.warn("Circuit Breaker Open or Error. Fallback triggered. Reason: {}", t.getMessage());
        return new SmsSendResult(false, "Fallback: Provider Unavailable - " + t.getMessage());
    }
}