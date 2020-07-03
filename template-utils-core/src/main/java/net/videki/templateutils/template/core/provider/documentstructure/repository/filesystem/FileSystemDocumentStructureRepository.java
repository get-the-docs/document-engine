package net.videki.templateutils.template.core.provider.documentstructure.repository.filesystem;

import net.videki.templateutils.template.core.configuration.DocumentStructureRepositoryConfiguration;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.provider.documentstructure.DocumentStructureRepository;
import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureBuilder;
import net.videki.templateutils.template.core.provider.templaterepository.filesystem.FileSystemTemplateRepository;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

public class FileSystemDocumentStructureRepository implements DocumentStructureRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemDocumentStructureRepository.class);

    private DocumentStructureBuilder documentStructureBuilder;
    private String basedir;

    @Override
    public void init(final DocumentStructureRepositoryConfiguration configuration) {
        this.basedir = configuration.getBaseDir();
        this.documentStructureBuilder = configuration.getBuilder();
    }

    public DocumentStructure getDocumentStructure(final String documentStructureFile)
            throws TemplateServiceConfigurationException {
        final String pathToFile = this.basedir + File.separator + documentStructureFile;

        final InputStream dsAsStream = FileSystemDocumentStructureRepository.class
                .getClassLoader()
                .getResourceAsStream(pathToFile);

        if (dsAsStream == null) {
            final String msg = String.format("DocumentStructure not found. File: %s. ", documentStructureFile);
            LOGGER.error(msg);

            throw new TemplateProcessException("0578f1ec-a33b-4a73-af71-a14f0e55c0b9", msg);
        } else {
            LOGGER.debug("DocumentStructure found. File: {}. ", documentStructureFile);

            return this.documentStructureBuilder.build(dsAsStream);
        }

    }

}
