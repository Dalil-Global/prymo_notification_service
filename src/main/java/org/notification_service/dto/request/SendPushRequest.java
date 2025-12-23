package org.notification_service.dto.request;


import lombok.Data;

import java.util.UUID;

@Data
public class SendPushRequest {
    private UUID userId;
    private String deviceToken;
    private String title;
    private String body;
}

