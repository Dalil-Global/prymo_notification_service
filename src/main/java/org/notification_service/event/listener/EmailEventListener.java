package org.notification_service.event.listener;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.event.EmailNotificationEvent;
import org.notification_service.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final EmailService emailService;

    @KafkaListener(topics = "${notification.topic.email}", groupId = "email-consumers", containerFactory = "kafkaListenerContainerFactory")
    public void onEmailEvent(EmailNotificationEvent event) {
        // call existing email service; add a method that accepts EmailNotificationEvent
        emailService.sendEmail(event.getUserId(),
                event.getRecipientEmail(),
                event.getSubject(),
                event.getTemplateName(),
                event.getVariables());
    }
}
