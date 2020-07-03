package net.videki.templateutils.template.core.provider.templaterepository.filesystem;

import net.videki.templateutils.template.core.configuration.RepositoryConfiguration;
import net.videki.templateutils.template.core.provider.templaterepository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

public class FileSystemTemplateRepository implements TemplateRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemTemplateRepository.class);

    private String baseDir;

    @Override
    public void init(final RepositoryConfiguration props) {
        this.baseDir = props.getBaseDir();
    }

    public InputStream getTemplate(final String templateFile) {
        InputStream result;

        final String pathToFile = this.baseDir + File.separator + templateFile;
                result = FileSystemTemplateRepository.class.getClassLoader().getResourceAsStream(pathToFile);
        if (result == null) {
            LOGGER.error("Template not found. File: {}. ", pathToFile);
        } else {
            LOGGER.debug("Template found. File: {}. ", pathToFile);
        }

        return result;

    }

}
