package org.notification_service.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notification_service.dto.event.EmailNotificationEvent;
import org.notification_service.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailEventListener {

    private final EmailService emailService;

    @KafkaListener(topics = "${notification.topic.email}", groupId = "email-consumers", containerFactory = "kafkaListenerContainerFactory")
    public void onEmailEvent(EmailNotificationEvent event) {
        log.info("Received Email Event for: {}", event.getRecipientEmail());

        // ✅ FIX 4: Call the processing method, not the ingestion method
        emailService.processEmailDelivery(event);
    }
}