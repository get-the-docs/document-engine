package org.getthedocs.documentengine.core.provider.templaterepository.filesystem;

/*-
 * #%L
 * docs-core
 * %%
 * Copyright (C) 2021 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.getthedocs.documentengine.core.provider.FilesystemProvider;
import org.getthedocs.documentengine.core.provider.persistence.Page;
import org.getthedocs.documentengine.core.provider.persistence.Pageable;
import org.getthedocs.documentengine.core.provider.templaterepository.TemplateRepository;
import org.getthedocs.documentengine.core.service.exception.TemplateNotFoundException;
import org.getthedocs.documentengine.core.service.exception.TemplateProcessException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.core.template.descriptors.TemplateDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Filesystem-based template repository implementation.
 * @author Levente Ban
 */
public class FileSystemTemplateRepository extends FilesystemProvider implements TemplateRepository {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemTemplateRepository.class);

    /**
     * Configuration property key in the system properties (see document-engine.properties).
     */
    public static final String TEMPLATE_REPOSITORY_PROVIDER_BASEDIR = "repository.template.provider.basedir";

    /**
     * Entry point for repo initialization.
     * @param props the system properties (see document-engine.properties)
     */
    @Override
    public void init(final Properties props) throws TemplateServiceConfigurationException {
        if (props == null) {
            throw new TemplateServiceConfigurationException("0944c01d-ab2d-4960-a7a4-0e143e82935d",
                    "Null or invalid template properties caught.");
        }

        super.init( (String) props.get(TEMPLATE_REPOSITORY_PROVIDER_BASEDIR));

        initTemplateRepositoryDir();
    }

    private void initTemplateRepositoryDir() throws TemplateServiceConfigurationException {
        LOGGER.info("Checking template repository access...");
        try (final Stream<Path> ignored = Files.list(Paths.get(getBasePath()).toAbsolutePath())) {
                LOGGER.info("Template repository available.");
        } catch (final NoSuchFileException e) {
            try {
                Files.createDirectories(Paths.get(getBasePath()).toAbsolutePath());

                LOGGER.info("Template repository did not exist, created.");
            } catch (final IOException ex) {
                final String msg = "Cannot create template repository path";
                LOGGER.error("Could not initialize template repository. " + msg, ex);
                throw new TemplateServiceConfigurationException("833e27f3-6813-43f5-aafd-cd511aca3ae0", msg);
            }
        } catch (final InvalidPathException | IOException e) {
            final String msg = "Invalid template path";
            LOGGER.error("Could not initialize template repository. " + msg, e);
            throw new TemplateServiceConfigurationException("9bd97feb-1387-40f3-bc82-01961e26bf5f", msg);
        }
    }

    /**
     * Returns the requested page of the actual template documents. 
     * @param page the requested page.
     * @return the request page of templates if found. 
     * @throws TemplateServiceException thrown in case of query or repo configuration errors.
     */
    @Override
    public Page<TemplateDocument> getTemplates(final Pageable page) throws TemplateServiceException{

        try {
            final Page<TemplateDocument> result = new Page<>();

            final Path basePath = Paths.get(getBasePath()).toAbsolutePath();
            try (final Stream<Path> path = Files.walk(basePath)) {
                final List<TemplateDocument> items = path
                        .filter(file -> !Files.isDirectory(file))
                        .map(basePath::relativize)
                        .map(Path::toString)
                        .map(TemplateDocument::new)
                        .collect(Collectors.toList());

                if (page != null && page.isPaged()) {

                    final int endIndex = Math.min(page.getOffset() + page.getSize(), Math.max(items.size() - 1, 0));

                    if (page.getOffset() < items.size() && endIndex < items.size()) {
                        result.setData(items.subList(page.getOffset(), endIndex));
                    }
                    result.setNumber(page.getPage());
                    result.setSize(page.getSize());
                    result.setTotalElements((long) items.size());
                    result.setTotalPages((int) Math.ceil(Float.valueOf(result.getTotalElements()) / page.getSize()));
                } else {
                    result.setData(items);
                }

                return result;
            }
        } catch (final IOException e) {
            final  String msg = String.format("Error retrieving the template list - baseDir: [%s]", getBasePath());
            LOGGER.error(msg, e);

            throw new TemplateServiceException("68b79868-c05c-4d14-8d94-1d8815625c8f", msg);
        }
    }

    /**
     * Returns a template descriptor for the given id (template name).
     * The template version is omitted in this implementation.
     * @param id the template name (relative path from the basedir).
     * @param version document version - NOT USED in this implementation.
     * @param withBinary true to return the template binary also.
     * @return the template document if found.
     */
    @Override
    public Optional<TemplateDocument> getTemplateDocumentById(final String id, final String version, final boolean withBinary) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getTemplateDocumentById - {} (version handling not supported, omitting). binary: {}", id, withBinary);
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
                throw new TemplateNotFoundException("be3be068-78fc-49bc-a4ec-76db86bd1f55", String.format("Template not found: [%s]", id));
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("getTemplateDocumentById end - {}. result: {}", id, result);
            }
            return Optional.of(result);

        } catch (final TemplateNotFoundException e) {
            return Optional.empty();
        } catch (final IOException | TemplateServiceConfigurationException e) {
            throw new TemplateProcessException("5204f592-5f4a-4a86-9cdf-0d3c6912cf5f", String.format("Error reading the template - id: [%s]", id));
        }
    }

    /**
     * Returns a template for a given template name.
     * @param templateFile the template name (relative path from the basedir).
     * @return the template document as a stream if found.
     */
    public InputStream getTemplate(final String templateFile) throws TemplateServiceConfigurationException {
        InputStream result;

        try {
            final String pathToFile = getBasePath() + File.separator + templateFile;
            result = Paths.get(pathToFile).toUri().toURL().openStream();

            if (result == null) {
                LOGGER.error("Template not found. File: {}. ", pathToFile);
            } else {
                LOGGER.debug("Template found. File: {}. ", pathToFile);
            }
        } catch (final IOException e) {
            result = null;
        }

        return result;

    }

}
