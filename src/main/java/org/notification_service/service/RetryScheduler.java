package org.notification_service.service;

import jakarta.transaction.Transactional;
import org.notification_service.model.NotificationEntity;
import org.notification_service.model.NotificationStatus;
import org.notification_service.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;




@Component
public class RetryScheduler {

    private static final Logger log = LoggerFactory.getLogger(RetryScheduler.class);
    private static final int MAX_RETRIES = 3;

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public RetryScheduler(NotificationRepository notificationRepository,
                          NotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

        @Scheduled(fixedDelayString = "${notification.retry.delay:60000}")
        @Transactional
        public void retryFailedEmails() {
        List<NotificationEntity> failed = notificationRepository.findByStatusAndAttemptsLessThan(NotificationStatus.valueOf("FAILED"),  MAX_RETRIES);
        for (NotificationEntity n : failed) {
        try {
            if (!"EMAIL".equalsIgnoreCase(n.getChannel())) continue;

            notificationService.sendEmail(
                    n.getUserId(),
                    n.getRecipientEmail(), // ✅ safe now
                    "Retry: " + n.getType(),
                    n.getType(),
                    null
            );
        } catch (Exception ex) {
            log.error("Retry failed for notification {}: {}", n.getId(), ex.getMessage());
        }
    }
  }
}

