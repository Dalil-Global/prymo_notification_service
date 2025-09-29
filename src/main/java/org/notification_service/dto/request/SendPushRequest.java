package org.notification_service.dto.request;


import lombok.Data;

@Data
public class SendPushRequest {
    private String deviceToken;
    private String title;
    private String body;
}

