package net.videki.templateutils.api.document.service;

import java.util.Optional;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;

public interface DocumentStructureApiService {
    String TEMPLATE_PACKAGE_TEMPLATE_SEPARATOR = ":";
    String TEMPLATE_PACKAGE_SEPARATOR = ".";


    Page<DocumentStructure> getDocumentStructures(String id, Pageable page);

    Optional<DocumentStructure> getDocumentStructureById(String id);

    String postDocumentStructureGenerationJob(String id, Object body);
}
