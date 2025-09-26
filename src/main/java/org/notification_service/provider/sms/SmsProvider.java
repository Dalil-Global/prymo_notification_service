package org.notification_service.provider.sms;

public interface SmsProvider {
    SmsSendResult sendSms(String to, String message, String externalId);
}
