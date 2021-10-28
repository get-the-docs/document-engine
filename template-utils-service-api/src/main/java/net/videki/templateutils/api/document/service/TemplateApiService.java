package net.videki.templateutils.api.document.service;

import java.util.Optional;

import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.template.descriptors.TemplateDocument;

public interface TemplateApiService {
    String TEMPLATE_PACKAGE_TEMPLATE_SEPARATOR = ":";
    String TEMPLATE_PACKAGE_SEPARATOR = ".";

    Page<TemplateDocument> getTemplates(Pageable page);

    Optional<TemplateDocument> getTemplateById(String id);

    String postTemplateGenerationJob(String id, Object body);

}
