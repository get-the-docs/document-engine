package net.videki.templateutils.service.service;

public interface TemplateApiService {
    String TEMPLATE_PACKAGE_TEMPLATE_SEPARATOR = ":";
    String TEMPLATE_PACKAGE_SEPARATOR = ".";

    String postTemplateGenerationJob(final String id, final Object body);

    String postDocumentStructureGenerationJob(final String id, final Object body);
}
