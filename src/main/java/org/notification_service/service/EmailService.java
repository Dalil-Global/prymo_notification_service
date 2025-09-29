package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendEmailRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.model.Notification;
import org.notification_service.model.NotificationStatus;
import org.notification_service.repository.NotificationRepository;
import org.notification_service.template.EmailTemplateProcessor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private final EmailTemplateProcessor emailTemplateProcessor;
    private final DeliveryTrackingService deliveryTrackingService;

    public NotificationResponse sendEmail(SendEmailRequest request) {
        return null;
    }

    public void send(SendEmailRequest req) {
    }
}
