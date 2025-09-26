package org.notification_service.provider.email;

import org.notification_service.dto.request.SendEmailRequest;

public interface EmailProvider {
    EmailSendResult sendEmail(SendEmailRequest request);
}
