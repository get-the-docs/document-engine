package net.videki.templateutils.template.core.provider.templaterepository.filesystem;

import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.provider.templaterepository.TemplateRepository;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import net.videki.templateutils.template.core.template.descriptors.TemplateDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

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

    @Override
    public Page<TemplateDocument> getTemplates(final Pageable page) throws TemplateServiceException{

        try {
            final Page<TemplateDocument> result = new Page<>();

            final Path basePath = Paths.get(this.baseDir).toAbsolutePath();
            final List<TemplateDocument> items = Files.walk(basePath)
              .filter(file -> !Files.isDirectory(file))
              .map(basePath::relativize)
              .map(Path::toString)
              .map(t -> new TemplateDocument(t))
              .collect(Collectors.toList());

            if (page != null && page.isPaged()) {
                result.setData(items.subList(page.getOffset(), Math.min(page.getOffset() + page.getSize(), Math.max(items.size()-1, 0))));
            } else {
                result.setData(items);
            }
            result.setNumber(page.getPage());
            result.setSize(page.getSize());
            result.setTotalElements(Long.valueOf(items.size()));
            result.setTotalPages((int) Math.ceil(Float.valueOf(result.getTotalElements()) / page.getSize()));

            return result;

        } catch (final IOException e) {
            final  String msg = String.format("Error retrieving the template list - baseDir: [{}]", this.baseDir);
            LOGGER.error(msg, e);

            throw new TemplateServiceException("68b79868-c05c-4d14-8d94-1d8815625c8f", msg);
        }
    }

    @Override
    public Optional<TemplateDocument> getTemplateDocumentById(final String id, final String version, final boolean withBinary) throws TemplateServiceException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getTemplateDocumentById - {} (version handling not supported, omiting). binary: {}", id, withBinary);
        }

        final TemplateDocument result = new TemplateDocument(id, version);

        try (final InputStream is = getTemplate(id)) {

            if (is != null) {
                if (withBinary) {
                    result.setBinary(is.readAllBytes());
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("getTemplateDocumentById - template not found: {}", id);
                }
                throw new TemplateNotFoundException("be3be068-78fc-49bc-a4ec-76db86bd1f55", String.format("Template not found: {]", id));
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("getTemplateDocumentById end - {}. result: {}", id, result);
            }
            return Optional.of(result);

        } catch (final TemplateNotFoundException e) {
            return Optional.empty();
        } catch (final IOException e) {
            throw new TemplateProcessException("5204f592-5f4a-4a86-9cdf-0d3c6912cf5f", String.format("Error reading the template - id: {}", id));
        }
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
