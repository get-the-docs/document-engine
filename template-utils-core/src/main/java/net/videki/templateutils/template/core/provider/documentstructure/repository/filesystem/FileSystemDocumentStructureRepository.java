package net.videki.templateutils.template.core.provider.documentstructure.repository.filesystem;

/*-
 * #%L
 * template-utils-core
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

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.provider.documentstructure.DocumentStructureRepository;
import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureBuilder;
import net.videki.templateutils.template.core.provider.documentstructure.builder.yaml.YmlDocStructureBuilder;
import net.videki.templateutils.template.core.provider.persistence.Page;
import net.videki.templateutils.template.core.provider.persistence.Pageable;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * File system based document structure repository.
 * 
 * @author Levente Ban
 */
public class FileSystemDocumentStructureRepository implements DocumentStructureRepository {
    /**
     * Configuration property key for the basedir where the document structures will
     * be stored.
     */
    private static final String DOCUMENT_STRUCTURE_PROVIDER_BASEDIR = "repository.documentstructure.provider.filesystem.basedir";

    /**
     * Configuration property key for the builder implementation to (de)serialize
     * the document structure descriptors.
     */
    private static final String DOCUMENT_STRUCTURE_BUILDER = "repository.documentstructure.builder";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemDocumentStructureRepository.class);

    /**
     * The configured document structure builder.
     */
    private DocumentStructureBuilder documentStructureBuilder;

    /**
     * The actual basedir.
     */
    private String basedir;

    /**
     * Initializes the repository based on the system properties caught.
     * 
     * @param props the system properties (see template-utils.properties)
     * @throws TemplateServiceConfigurationException thrown in case of configuration
     *                                               errors.
     */
    @Override
    public void init(final Properties props) throws TemplateServiceConfigurationException {
        if (props == null) {
            throw new TemplateServiceConfigurationException("28f5041e-f1db-483d-97ec-fd3bda9bc11c",
                    "Null or invalid template properties caught.");
        }

        this.basedir = (String) props.get(DOCUMENT_STRUCTURE_PROVIDER_BASEDIR);
        this.documentStructureBuilder = loadDocumentStructureBuilder(props);
    }

    /**
     * Entry point for repository initialization.
     * @param props configuration properties (see template-utils.properties) 
     * @throws TemplateServiceConfigurationException thrown in case of initialization errors based on configuration errors. 
     */    
    @Override
    public Page<DocumentStructure> getDocumentStructures(Pageable page) throws TemplateServiceException {

        try {
            final Page<DocumentStructure> result = new Page<>();

            final Path basePath = Paths.get(this.basedir).toAbsolutePath();
            final List<DocumentStructure> items = Files.walk(basePath)
              .filter(file -> !Files.isDirectory(file))
              .map(basePath::relativize)
              .map(Path::toString)
              .map(t -> new DocumentStructure(t))
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
            final  String msg = String.format("Error retrieving the document structure list - baseDir: [{}]", this.basedir);
            LOGGER.error(msg, e);

            throw new TemplateServiceException("71117d2a-9f36-49f5-af25-ddfbca07cea3", msg);
        }
    }

    /**
     * Returns a document structure by its id in the current repository (a
     * descriptor named as the document structure id).
     * 
     * @param documentStructureFile the document structure id (is equal to the
     *                              filename).
     * @return the document structure descriptor if the provided id was found.
     * @throws TemplateServiceConfigurationException thrown in case of repository
     *                                               configuration errors.
     */
    @Override
    public DocumentStructure getDocumentStructure(final String documentStructureFile)
            throws TemplateServiceConfigurationException {
        final String pathToFile = this.basedir + File.separator + documentStructureFile;

        InputStream dsAsStream;
        try {
            dsAsStream = Paths.get(pathToFile).toUri().toURL().openStream();
        } catch (final IOException e) {
            dsAsStream = null;
        }

        if (dsAsStream == null) {
            final String msg = String.format("DocumentStructure not found. File: %s. ", documentStructureFile);
            LOGGER.error(msg);

            throw new TemplateProcessException("0578f1ec-a33b-4a73-af71-a14f0e55c0b9", msg);
        } else {
            LOGGER.debug("DocumentStructure found. File: {}. ", documentStructureFile);

            return this.documentStructureBuilder.build(dsAsStream);
        }
    }

    /**
     * Factory method to initialize the descriptor builder implementation.
     * 
     * @param props the system properties (see template-utils.properties)
     * @return the builder instance
     */
    private DocumentStructureBuilder loadDocumentStructureBuilder(final Properties props) {
        DocumentStructureBuilder documentStructureBuilder = new YmlDocStructureBuilder();

        String repositoryProvider = "<Not configured or could not read properties file>";
        try {
            repositoryProvider = (String) props.get(DOCUMENT_STRUCTURE_BUILDER);

            if (repositoryProvider != null) {
                documentStructureBuilder = (DocumentStructureBuilder) this.getClass().getClassLoader()
                        .loadClass(repositoryProvider).getDeclaredConstructor().newInstance();
            } else {
                final String msg = "Document structure builder not specified, "
                        + "using built-in YmlDocStructureBuilder.";
                LOGGER.info(msg);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
                | InvocationTargetException e) {
            final String msg = String.format(
                    "Error loading document structure builder: %s, " + "using built-in YmlDocStructureBuilder.",
                    repositoryProvider);
            LOGGER.error(msg, e);
        }

        return documentStructureBuilder;
    }
}
