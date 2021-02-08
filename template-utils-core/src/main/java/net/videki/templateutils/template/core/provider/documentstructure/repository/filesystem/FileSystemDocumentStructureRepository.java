package net.videki.templateutils.template.core.provider.documentstructure.repository.filesystem;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.provider.documentstructure.DocumentStructureRepository;
import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureBuilder;
import net.videki.templateutils.template.core.provider.documentstructure.builder.yaml.YmlDocStructureBuilder;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class FileSystemDocumentStructureRepository implements DocumentStructureRepository {
    private static final String DOCUMENT_STRUCTURE_PROVIDER_BASEDIR =
            "repository.documentstructure.provider.filesystem.basedir";
    private static final String DOCUMENT_STRUCTURE_BUILDER = "repository.documentstructure.builder";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemDocumentStructureRepository.class);

    private DocumentStructureBuilder documentStructureBuilder;
    private String basedir;

    @Override
    public void init(final Properties props) throws TemplateServiceConfigurationException {
        if (props == null) {
            throw new TemplateServiceConfigurationException("28f5041e-f1db-483d-97ec-fd3bda9bc11c",
                    "Null or invalid template properties caught.");
        }

        this.basedir = (String) props.get(DOCUMENT_STRUCTURE_PROVIDER_BASEDIR);
        this.documentStructureBuilder = loadDocumentStructureBuilder(props);
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

    private DocumentStructureBuilder loadDocumentStructureBuilder(final Properties props) {
        DocumentStructureBuilder documentStructureBuilder = new YmlDocStructureBuilder();

        String repositoryProvider = "<Not configured or could not read properties file>";
        try {
            repositoryProvider = (String) props.get(DOCUMENT_STRUCTURE_BUILDER);

            if (repositoryProvider != null) {
                documentStructureBuilder = (DocumentStructureBuilder)
                        this.getClass().getClassLoader()
                                .loadClass(repositoryProvider)
                                .getDeclaredConstructor()
                                .newInstance();
            } else {
                final String msg = "Document structure builder not specified, " +
                        "using built-in YmlDocStructureBuilder.";
                LOGGER.info(msg);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException |
                NoSuchMethodException | InvocationTargetException e) {
            final String msg = String.format("Error loading document structure builder: %s, " +
                    "using built-in YmlDocStructureBuilder.", repositoryProvider);
            LOGGER.error(msg, e);
        }

        return documentStructureBuilder;
    }
}
