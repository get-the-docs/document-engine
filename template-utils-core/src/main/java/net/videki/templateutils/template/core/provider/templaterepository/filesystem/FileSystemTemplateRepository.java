package net.videki.templateutils.template.core.provider.templaterepository.filesystem;

import net.videki.templateutils.template.core.provider.templaterepository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class FileSystemTemplateRepository implements TemplateRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemTemplateRepository.class);

    public InputStream getTemplate(final String templateFile) {
        InputStream result;

        result = FileSystemTemplateRepository.class.getResourceAsStream(templateFile);
        if (result == null) {
            LOGGER.error("Template not found. File: {}. ", templateFile);
        } else {
            LOGGER.debug("Template found. File: {}. ", templateFile);
        }

        return result;

    }

}
