package net.videki.templateutils.template.core.provider.templaterepository.filesystem;

import net.videki.templateutils.template.core.provider.templaterepository.TemplateRepository;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class FileSystemTemplateRepository implements TemplateRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemTemplateRepository.class);

    private static final String TEMPLATE_REPOSITORY_PROVIDER_BASEDIR = "repository.template.provider.basedir";

    private String baseDir;

    @Override
    public void init(final Properties props) throws TemplateServiceConfigurationException {
        if (props == null) {
            throw new TemplateServiceConfigurationException("0944c01d-ab2d-4960-a7a4-0e143e82935d",
                    "Null or invalid template properties caught.");
        }

        this.baseDir = (String) props.get(TEMPLATE_REPOSITORY_PROVIDER_BASEDIR);
    }

    public InputStream getTemplate(final String templateFile) {
        InputStream result;

        final String pathToFile = this.baseDir + File.separator + templateFile;
        try {
            result = Paths.get(pathToFile).toUri().toURL().openStream();
        } catch (final IOException e) {
            result = null;
        }
//                result = FileSystemTemplateRepository.class.getClassLoader().getResourceAsStream(pathToFile);

        if (result == null) {
            LOGGER.error("Template not found. File: {}. ", pathToFile);
        } else {
            LOGGER.debug("Template found. File: {}. ", pathToFile);
        }

        return result;

    }

}
