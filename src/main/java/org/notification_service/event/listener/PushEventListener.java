package org.notification_service.event.listener;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.event.PushNotificationEvent;
import org.notification_service.service.PushNotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushEventListener {

    private final PushNotificationService pushService;

    @KafkaListener(topics = "${notification.topic.push}", groupId = "push-consumers", containerFactory = "kafkaListenerContainerFactory")
    public void onPushEvent(PushNotificationEvent event) {
        pushService.sendPush(event); // implement sendPush(PushNotificationEvent e)
    }
}