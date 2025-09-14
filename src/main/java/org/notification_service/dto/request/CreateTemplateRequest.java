package org.notification_service.dto.request;

import lombok.Data;

@Data
public class CreateTemplateRequest {
    private String name;
    private String type; // EMAIL
    private String content; // HTML body
}
