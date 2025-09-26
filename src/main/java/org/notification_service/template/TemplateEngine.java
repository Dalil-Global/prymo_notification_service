package org.notification_service.template;

import java.util.Map;

public interface TemplateEngine {

    String loadTemplate(String templatePath);
    /**
     * Renders a template with the provided variables.
     *
     * @param templateContent Raw template content (could be HTML, text, etc.)
     * @param variables Key-value pairs for template placeholders
     * @return Rendered content
     */
    String render(String templateContent, Map<String, Object> variables);
}