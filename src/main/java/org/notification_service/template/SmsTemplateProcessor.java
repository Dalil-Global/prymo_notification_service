package org.notification_service.template;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SmsTemplateProcessor implements TemplateEngine {

    @Override
    public String loadTemplate(String templatePath) {
        return "";
    }

    @Override
    public String render(String templateContent, Map<String, Object> variables) {
        String rendered = templateContent;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            rendered = rendered.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
        }
        return rendered;
    }
}