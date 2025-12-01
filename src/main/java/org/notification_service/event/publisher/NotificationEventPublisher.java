package org.notification_service.event.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationEventPublisher {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Value("${notification.topic.email}")
    private String emailTopic;

    @Value("${notification.topic.sms}")
    private String smsTopic;

    @Value("${notification.topic.push}")
    private String pushTopic;

    public void publishEmail(Object event) {
        kafkaTemplate.send(emailTopic, event);
    }

    public void publishSms(Object event) {
        kafkaTemplate.send(smsTopic, event);
    }

    public void publishPush(Object event) {
        kafkaTemplate.send(pushTopic, event);
    }
}

