package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendEmailRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.model.Notification;
import org.notification_service.model.NotificationStatus;
import org.notification_service.repository.NotificationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;

    public NotificationResponse sendEmail(SendEmailRequest request) {
        Notification notification = Notification.builder()
                .recipientEmail(request.getRecipientEmail())
                .subject(request.getSubject())
                .body(request.getBody())
                .status(NotificationStatus.PENDING)
                .build();

        notification = notificationRepository.save(notification);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getRecipientEmail());
            message.setSubject(request.getSubject());
            message.setText(request.getBody());

            mailSender.send(message);

            notification.setStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
        }

        notificationRepository.save(notification);

        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientEmail(notification.getRecipientEmail())
                .subject(notification.getSubject())
                .status(notification.getStatus().name())
                .build();
    }
}