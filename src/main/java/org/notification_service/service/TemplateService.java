package org.notification_service.service;

import org.notification_service.model.NotificationTemplate;
import org.notification_service.repository.NotificationTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class TemplateService {

    private final NotificationTemplateRepository templateRepository;

    public TemplateService(NotificationTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public NotificationTemplate createTemplate(String name, String type, String content) {
        NotificationTemplate template = NotificationTemplate.builder()
                .name(name)
                .type(type)
                .content(content)
                .build();
        return templateRepository.save(template);
    }

    public Optional<NotificationTemplate> findByName(String name) {
        return templateRepository.findByName(name);
    }

    /**
     * Renders a template content replacing placeholders of the form {{key}}.
     */
    public String render(String rawTemplate, Map<String, Object> variables) {
        if (variables == null || variables.isEmpty()) {
            return rawTemplate;
        }
        String rendered = rawTemplate;
        for (Map.Entry<String, Object> e : variables.entrySet()) {
            String placeholder = "{{" + e.getKey() + "}}";
            rendered = rendered.replace(placeholder, e.getValue() == null ? "" : e.getValue().toString());
        }
        return rendered;
    }
}