package org.notification_service.template;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Component
public class EmailTemplateProcessor implements TemplateEngine {

    @Override
    public String loadTemplate(String templatePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(templatePath)));
        } catch (IOException e) {
            throw new RuntimeException("Error loading email template: " + templatePath, e);
        }
    }

    @Override
    public String render(String templateContent, Map<String, Object> variables) {
        String rendered = templateContent;
        if (variables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}"; // e.g. {{name}}
                rendered = rendered.replace(placeholder, entry.getValue().toString());
            }
        }
        return rendered;
    }
}
