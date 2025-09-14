package org.notification_service.controller;

import lombok.RequiredArgsConstructor;
import org.notification_service.dto.request.SendEmailRequest;
import org.notification_service.dto.response.NotificationResponse;
import org.notification_service.service.EmailService;
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

    @PostMapping("/send-email")
    public ResponseEntity<NotificationResponse> sendEmail(@RequestBody SendEmailRequest request) {
        return ResponseEntity.ok(emailService.sendEmail(request));
    }
}
