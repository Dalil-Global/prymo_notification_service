package org.notification_service.event.listener;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.event.SmsNotificationEvent;
import org.notification_service.service.SmsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsEventListener {

    private final SmsService smsService;

    @KafkaListener(topics = "${notification.topic.sms}", groupId = "sms-consumers", containerFactory = "kafkaListenerContainerFactory")
    public void onSmsEvent(SmsNotificationEvent event) {
        smsService.processSmsDelivery(event);
    }
}