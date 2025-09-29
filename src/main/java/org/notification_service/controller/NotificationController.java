package org.notification_service.controller;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendEmailRequest;
import org.notification_service.dto.request.SendPushRequest;
import org.notification_service.dto.request.SendSmsRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.service.EmailService;
import org.notification_service.service.NotificationService;
import org.notification_service.service.PushNotificationService;
import org.notification_service.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {


    private final EmailService emailService;
    private final SmsService smsService;
    private final PushNotificationService pushNotificationService;

    @PostMapping("/send-email")
    public ResponseEntity<NotificationResponse> sendEmail(@RequestBody SendEmailRequest request) {
        NotificationResponse response = emailService.sendEmail(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/send-sms")
    public ResponseEntity<NotificationResponse> sendSms(@RequestBody SendSmsRequest request) {
        NotificationResponse response  = smsService.sendSms(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/push")
    public ResponseEntity<NotificationResponse> sendPush(@RequestBody SendPushRequest request) {
        NotificationResponse response = pushNotificationService.sendPush(request);
        return ResponseEntity.ok(response);
    }
}
