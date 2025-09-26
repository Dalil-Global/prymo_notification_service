package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendSmsRequest;
import org.notification_service.model.Notification;
import org.notification_service.model.NotificationStatus;
import org.notification_service.model.UserNotificationPreference;
import org.notification_service.provider.sms.SmsProvider;
import org.notification_service.provider.sms.SmsSendResult;
import org.notification_service.repository.NotificationRepository;
import org.notification_service.repository.UserPreferenceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsProvider smsProvider; // TermiiSmsProvider injected
    private final NotificationRepository notificationRepository;
    private final UserPreferenceRepository preferenceRepository;

    public Notification sendSmsForUserId(java.util.UUID userId, SendSmsRequest request) {
        // Check user preferences: if exists and sms disabled, skip
        Optional<UserNotificationPreference> prefOpt = preferenceRepository.findByUserId(userId);
        if (prefOpt.isPresent() && Boolean.FALSE.equals(prefOpt.get().getSmsEnabled())) {
            // Optionally, return a notification record with PENDING or mark as SKIPPED
            Notification skipped = Notification.builder()
                    .recipientPhone(request.getRecipientPhone())
                    .body(request.getMessage())
                    .status(NotificationStatus.FAILED) // or a SKIPPED enum if you prefer
                    .build();
            return notificationRepository.save(skipped);
        }

        return sendSms(request);
    }

    public Notification sendSms(SendSmsRequest request) {
        Notification n = Notification.builder()
                .recipientPhone(request.getRecipientPhone())
                .body(request.getMessage())
                .status(NotificationStatus.PENDING)
                .build();
        n = notificationRepository.save(n);

        SmsSendResult res = smsProvider.sendSms(request.getRecipientPhone(), request.getMessage(), request.getExternalId());

        if (res.isSuccess()) {
            n.setStatus(NotificationStatus.SENT);
        } else {
            n.setStatus(NotificationStatus.FAILED);
        }
        // optionally store provider response in body or another column later
        notificationRepository.save(n);
        return n;
    }
}