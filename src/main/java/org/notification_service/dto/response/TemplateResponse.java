package org.notification_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TemplateResponse {
    private UUID id;
    private String name;
    private String type;
    private String content;
}
