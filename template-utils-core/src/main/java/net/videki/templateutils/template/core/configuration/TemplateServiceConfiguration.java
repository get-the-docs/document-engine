package net.videki.templateutils.template.core.configuration;

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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import net.videki.templateutils.template.core.processor.input.InputTemplateProcessor;
import net.videki.templateutils.template.core.provider.documentstructure.DocumentStructureRepository;
import net.videki.templateutils.template.core.provider.resultstore.ResultStore;
import net.videki.templateutils.template.core.provider.templaterepository.TemplateRepository;
import net.videki.templateutils.template.core.provider.templaterepository.filesystem.FileSystemTemplateRepository;
import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.videki.templateutils.template.core.service.TemplateService;

import static net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException.MSG_INVALID_PARAMETERS;

/**
 * Template service configuration.
 * The default behaviour can be configured by adding a template-utils.properties config file to the classloader root.
 *
 * <p>
 *     <b>Config options</b>
 * </p>
 * <p>#                                                                                                                 </p>
 * <p># -------------------------                                                                                       </p>
 * <p># <b>Template-utils properties</b>                                                                                </p>
 * <p># -------------------------                                                                                       </p>
 * <p>#                                                                                                                 </p>
 * <p># <b>Document structure and value object logging category</b>                                                     </p>
 * <p># -----------------------------                                                                                   </p>
 * <p>#   If specified, the document structure and the actual value set can be logged with a separate logger.           </p>
 * <p>#   To do so, enable a log category specified by this setting.                                                    </p>
 * <p>#   The logging is bound to DEBUG log level and is logged by default through the TemplateService's logger.        </p>
 * <p># common.log.value-logcategory= net.videki.templateutils.valuelog                                                 </p>
 * <p>#                                                                                                                 </p>
 * <p># <b>Template repository provider class</b>                                                                       </p>
 * <p># Available providers: FileSystemTemplateRepository (default) can load template available                         </p>
 * <p># for the context classloader.                                                                                    </p>
 * <p>#   in server environments if you do not want to bundle the available templates into the application,             </p>
 * <p>#   add the template files path to the container classpath and address them in the descriptors with               </p>
 * <p>#   their plain path.                                                                                             </p>
 * <p>#   To use your own implement the net.videki.templateutils.template.core.provider.TemplateRepository interface    </p>
 * <p>repository.template.provider=net.videki.templateutils.template.core.provider.templaterepository.filesystem.FileSystemTemplateRepository          </p>
 * <p># Specifies the directory containing your template files (if set, this will be added to the template filename as a parent path)                  </p>
 * <p>repository.template.provider.basedir=templates                                                                    </p>
 * <p>#                                                                                                                 </p>
 * <p># <b>Result repository provider class</b>                                                                         </p>
 * <p># This setting specifies where to store the generated documents.                                                  </p>
 * <p># By default the filesystem is used, but you can specify another directory (for example on a temporary fs)        </p>
 * <p>repository.result.provider=net.videki.templateutils.template.core.provider.resultstore.filesystem.FileSystemResultStore       </p>
 * <p>repository.result.provider.FileSystemResultStore.basedir=target/test-results/generated-documents                  </p>
 * <p>#                                                                                                                             </p>
 * <p># <b>Converters/PDF - Font library</b>                                                                            </p>
 * <p># -----------------------------                                                                                   </p>
 * <p>#   For non built-in fonts (other then COURIER, HELVETICA, TIMES_ROMAN) the fonts used by the source documents    </p>
 * <p>#   have to be provided.                                                                                          </p>
 * <p>#   The fonts will be embedded for the correct appearance into the result document.                               </p>
 * <p>#   This setting is important to deal with codepage related PDF problems, but take care of the font licences      </p>
 * <p>#   before deploying them in any environment.                                                                     </p>
 * <p>#                                                                                                                 </p>
 * <p>#   Usage:                                                                                                        </p>
 * <p>#     the fonts have to be specified as shown in the example below and                                            </p>
 * <p>#     placed in a directory accessible for the TemplateService's class loader (e.g. have to be on the classpath). </p>
 * <p>#                                                                                                                 </p>
 * <p># Font base directory                                                                                             </p>
 * <p>converter.pdf.font-library.basedir=/templates/fonts                                                               </p>
 * <p>#                                                                                                                 </p>
 * <p>converter.pdf.font-library.font.arial.bold=arialbd.ttf                                                            </p>
 * <p>converter.pdf.font-library.font.arial.italic=ariali.ttf                                                           </p>
 * <p>converter.pdf.font-library.font.arial.boldItalic=arialbi.ttf                                                      </p>
 * <p>converter.pdf.font-library.font.arial.normal=arial.ttf                                                            </p>
 * <p>#                                                                                                                 </p>
 * <p>converter.pdf.font-library.font.calibri.bold=calibrib.ttf                                                         </p>
 * <p>converter.pdf.font-library.font.calibri.italic=calibrii.ttf                                                       </p>
 * <p>converter.pdf.font-library.font.calibri.boldItalic=calibriz.ttf                                                   </p>
 * <p>converter.pdf.font-library.font.calibri.normal=calibri.ttf                                                        </p>
 * <p>#                                                                                                                 </p>
 * <p>converter.pdf.font-library.font.tahoma.bold=tahomabd.ttf                                                          </p>
 * <p>converter.pdf.font-library.font.tahoma.italic=tahoma.ttf                                                          </p>
 * <p>converter.pdf.font-library.font.tahoma.boldItalic=tahomabd.ttf                                                    </p>
 * <p>converter.pdf.font-library.font.tahoma.normal=tahoma.ttf                                                          </p>
 * <p>#                                                                                                                 </p>
 * <p>converter.pdf.font-library.font.times.bold=timesbd.ttf                                                            </p>
 * <p>converter.pdf.font-library.font.times.italic=timesi.ttf                                                           </p>
 * <p>converter.pdf.font-library.font.times.boldItalic=timesbi.ttf                                                      </p>
 * <p>converter.pdf.font-library.font.times.normal=times.ttf                                                            </p>
 *
 * @author Levente Ban
 */
public class TemplateServiceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

    private static TemplateServiceConfiguration INSTANCE = new  TemplateServiceConfiguration();

    private static final Object LOCKOBJECT = new Object();

    // template-utils.properties keys
    private static final String FONT_FAMILY = "converter.pdf.font-library.font";
    private static final String FONT_DIR = "converter.pdf.font-library.basedir";
    private static final String CONFIG_FILE_NAME = "template-utils.properties";
    private static final String LOG_APPENDER = "common.log.value-logcategory";

    private static final String CONFIG_ENV_FILENAME = "configFile";
    private static final String TEMPLATE_REPOSITORY_PROVIDER = "repository.template.provider";
    private static final String DOCUMENT_STRUCTURE_PROVIDER = "repository.documentstructure.provider";
    private static final String RESULT_REPOSITORY_PROVIDER = "repository.result.provider";
    private static final String PROCESSORS = "processors";

    private static final String PROPERTY_DELIMITER = ".";

    private Properties configurationProperties;
    private DocumentStructureRepository documentStructureRepository;

    private TemplateRepository templateRepository;

    private final List<InputTemplateProcessor> inputProcessors = new LinkedList<>();

    private ResultStore resultStore;

    private final Map<String, Map<FontStyle, FontConfig>> styles = new TreeMap<>();

    private String fontDir;

    private static final String MSG_ERROR_REPOSITORYPROVIDER_NOT_CONFIGURED =
            "<Not configured or could not read properties file>";
    private static final String MSG_FALLBACK_RESULTSTORE = "using fallback built-in FilesystemResultStore.";
    private static final String MSG_CONFIG_ERROR = "Configuration error.";


    /**
     * Initializes the template service configuration.
     * The config is read from the first file on the classpath named template-utils.properties.
     */
    protected TemplateServiceConfiguration() {
        init();
    }

    /**
     * Initializes the template service.
     */
    protected void init() {

        final Properties properties = new Properties();
        try {
            final var configPath = System.getenv(CONFIG_ENV_FILENAME);
            URL path = getClass().getClassLoader().getResource(CONFIG_FILE_NAME);
            if (StringUtils.isNotEmpty(configPath)) {
                path = new File(configPath).toURI().toURL();
            }

            if (path != null) {
                try (final InputStream propFile = path.openStream()) {
                    properties.load(propFile);

                    LOGGER.info("template-utils.properties configuration file found at location: {}", path.getPath());

                    initFontLibrary(properties);
                    initDocumentStructureRepository(properties);
                    initTemplateRepository(properties);
                    initResultStore(properties);
                    initProcessors(properties);

                    this.configurationProperties = properties;

                }
            } else {
                LOGGER.error("No template-utils.properties configuration file found");
            }
        } catch (final Exception e) {
            LOGGER.error("template-utils.properties configuration file not found, using default configuration.");
        }
    }

    /**
     * Initialzes the custom fonts for pdf conversion.
     * @param properties System properties (see config file - template-utils.properties).
     */
    private void initFontLibrary(final Properties properties) {
        if (properties != null && !properties.keySet().isEmpty()) {
            this.fontDir = (String) properties.get(FONT_DIR);

            for (final String s : properties.keySet().stream()
                    .map(s -> (String) s)
                    .filter(s -> s.startsWith(FONT_FAMILY) && s.split("\\.").length == 5)
                    .toList()) {

                final String[] parts = s.split("\\.");

                final String actFamily = parts[4];

                for (final FontStyle fs : FontStyle.values()) {
                    final String fileForStyle = (String) properties.get(s + PROPERTY_DELIMITER + fs);
                    final FontConfig f = new FontConfig();
                    f.setFontFamily(actFamily);
                    f.setStyle(fs);
                    f.setBasedir(this.fontDir);
                    f.setFileName(fileForStyle);

                    Map<FontStyle, FontConfig> fm = styles.computeIfAbsent(actFamily, k -> new TreeMap<>());
                    fm.put(fs, f);
                }

            }
        }
    }

    /**
     * Initializes the document structure repository for the implementation class configured in the configuration parameters.
     */
    private void initDocumentStructureRepository(final Properties properties) {
        String repositoryProvider = MSG_ERROR_REPOSITORYPROVIDER_NOT_CONFIGURED;
        try {
            repositoryProvider = (String) properties.get(DOCUMENT_STRUCTURE_PROVIDER);

            if (repositoryProvider != null) {
                final DocumentStructureRepository tmpRepo = (DocumentStructureRepository)
                        this.getClass().getClassLoader()
                                .loadClass(repositoryProvider)
                                .getDeclaredConstructor()
                                .newInstance();
                tmpRepo.init(properties);

                this.documentStructureRepository = tmpRepo;
            } else {
                final String msg = "Document structure repository not specified, " + MSG_FALLBACK_RESULTSTORE;
                LOGGER.info(msg);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            final String msg = String.format("Error loading document structure repository: %s, "
                    + MSG_FALLBACK_RESULTSTORE, repositoryProvider);
            LOGGER.error(msg, e);

            this.templateRepository = new FileSystemTemplateRepository();
        } catch (final TemplateServiceConfigurationException e) {
            final String msg = MSG_CONFIG_ERROR;
            LOGGER.error(msg, e);

            throw new TemplateServiceRuntimeException(msg);
        } catch (InvocationTargetException e) {
            LOGGER.error("Cannot load document structure repository due configuration errors.", e);
        }
    }

    /**
     * Initializes the template repository for the implementation class configured in the configuration parameters.
     */
    private void initTemplateRepository(final Properties properties) {
        String repositoryProvider = MSG_ERROR_REPOSITORYPROVIDER_NOT_CONFIGURED;
        try {
            repositoryProvider = (String) properties.get(TEMPLATE_REPOSITORY_PROVIDER);

            if (repositoryProvider != null) {
                final TemplateRepository tmpRepo = (TemplateRepository)
                        this.getClass().getClassLoader()
                                .loadClass(repositoryProvider)
                                .getDeclaredConstructor()
                                .newInstance();

                tmpRepo.init(properties);

                this.templateRepository = tmpRepo;
            } else {
                final String msg = "Template repository not specified, " + MSG_FALLBACK_RESULTSTORE;
                LOGGER.info(msg);
            }
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            final String msg = String.format("Error loading template repository: %s, "
                    + MSG_FALLBACK_RESULTSTORE, repositoryProvider);
            LOGGER.error(msg, e);

            this.templateRepository = new FileSystemTemplateRepository();
        } catch (final TemplateServiceConfigurationException e) {
            final String msg = MSG_CONFIG_ERROR;
            LOGGER.error(msg, e);

            throw new TemplateServiceRuntimeException(msg);
        }
    }

    /**
     * Initializes the result store repository for the implementation class configured in the configuration parameters.
     */
    private void initResultStore(final Properties properties) {
        String repositoryProvider = MSG_ERROR_REPOSITORYPROVIDER_NOT_CONFIGURED;
        try {
            repositoryProvider = (String) properties.get(RESULT_REPOSITORY_PROVIDER);

            if (repositoryProvider != null) {
                ResultStore tmpRepo = (ResultStore)
                        this.getClass().getClassLoader()
                                .loadClass(repositoryProvider)
                                .getDeclaredConstructor()
                                .newInstance();

                tmpRepo.init(properties);

                this.resultStore = tmpRepo;
            } else {
                final String msg = "Template result repository not specified, " + MSG_FALLBACK_RESULTSTORE;
                LOGGER.info(msg);

            }
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException |
                NoSuchMethodException | InvocationTargetException e) {
            final String msg = String.format("Error loading result store repository: %s, " +
                    "using fallback built-in FilesystemResultStore.", repositoryProvider);
            LOGGER.error(msg, e);
        } catch (final TemplateServiceConfigurationException e) {
            final String msg = MSG_CONFIG_ERROR;
            LOGGER.error(msg, e);

            throw new TemplateServiceRuntimeException(msg);
        }
    }

    /**
     * Initializes the document processor the implementation classes configured in the configuration parameters.
     */
    private void initProcessors(final Properties properties) {
        try {
            if (properties != null) {
                final Set<Object> processors =
                        properties.keySet()
                                .stream()
                                .filter(o -> ((String) o).startsWith(PROCESSORS))
                                .collect(Collectors.toSet());

                for (final var o : processors) {
                    final var actProcessor = (String) properties.get(o);
                    LOGGER.info("Using template processor: {}", actProcessor);

                    InputTemplateProcessor tmpProcessor = (InputTemplateProcessor)
                            this.getClass().getClassLoader()
                                    .loadClass(actProcessor)
                                    .getDeclaredConstructor()
                                    .newInstance();

                    this.inputProcessors.add(tmpProcessor);
                }
            } else {
                final String msg = "Empty config caught.";
                LOGGER.error(msg);

            }
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException |
                NoSuchMethodException | InvocationTargetException e) {
            final String msg = String.format("Error loading template processors. %s", e);
            LOGGER.error(msg, e);

            throw new TemplateServiceRuntimeException(msg);
        }
    }

/*
    private static InputStream getResource(String filename) throws java.io.IOException {
        java.net.URL url = TemplateServiceConfiguration.class.getClassLoader().getResource(filename);

        if (url == null) {
            LOGGER.warn("Couldn't get resource: " + filename);
            throw new IOException(filename + " not found via classloader.");
        }

        InputStream is = url.openConnection().getInputStream();
        if (is == null) {
            LOGGER.error("Couldn't open file: " + filename);
            throw new IOException(filename + " not found via classloader.");
        }
        return is;
    }
*/

    /**
     * Returns whether there is a font configuration in the custom font library configured in the system properties. 
     * The font config is used by the pdf converter engine (if configured).
     * @param familyName the font family to be added.
     * @param style the font style.
     * @return the font configuration DTO, if found.
     */
    public FontConfig getFontConfig(final String familyName, final FontStyle style) {
        Set<String> families = styles.keySet();
        for (final String actKey : families) {
            if (familyName.toLowerCase().contains(actKey)) {
                final Map<FontStyle, FontConfig> fm = styles.get(actKey);

                return fm.get(style);
            }
        }

        return null;
    }

    /**
     * Returns the custom fonts installed to the font lib dir and configured in the system properties.
     * @return the list of the configured additional fonts.
     */
    public List<FontConfig> getFontConfig() {
        final List<FontConfig> result = new LinkedList<>();

        final Set<String> families = this.styles.keySet();
        for (final String actKey : families) {
            final Map<FontStyle, FontConfig> fm = this.styles.get(actKey);
            result.addAll(fm.values());
        }

        return result;
    }

    /**
     * Returns the log category of the document structure handling implementations. 
     * Is used by the doc structure implementations to ensure consistent logging.
     * @return the documentstructure log category.
     */
    public String getDocStructureLogCategory() {
        if (this.configurationProperties != null) {
           return this.configurationProperties.getProperty(LOG_APPENDER);
        } else {
            return "";
        }
    }

    /**
     * Returns the configured document structure repository implementation.
     * For configuration options see the system configuration (template-utils.properties)
     * @return the document structure implementation.
     */
    public DocumentStructureRepository getDocumentStructureRepository() {
        return documentStructureRepository;
    }

    /**
     * Returns the template repository implementation.
     * For configuration options see the system configuration (template-utils.properties)
     * @return the template repository implementation.
     */
    public TemplateRepository getTemplateRepository() {
        return this.templateRepository;
    }

    /**
     * Returns the result store repository implementation.
     * For configuration options see the system configuration (template-utils.properties)
     * @return the result store repository implementation.
     */
    public ResultStore getResultStore() {
        return this.resultStore;
    }

    /**
     * Returns the actual template service configuration.
     * This is the entrypoint of the configuration.
     * @return the actual template servic configuration.
     */
    public static TemplateServiceConfiguration getInstance() {
        TemplateServiceConfiguration result = INSTANCE;
        if (result == null) {
            synchronized (LOCKOBJECT) {
                result = INSTANCE = new TemplateServiceConfiguration();
            }
        }

        return result;
    }

    /**
     * Returns the configured input processors.
     * @return inputProcessors
     */
    public Map<InputFormat, InputTemplateProcessor> getInputProcessors() {
        final Map<InputFormat, InputTemplateProcessor> result = new EnumMap<>(InputFormat.class);

        for (final InputTemplateProcessor actItem : this.inputProcessors) {
            result.putIfAbsent(actItem.getInputFormat(), actItem);
        }

        return result;
    }

    /**
     * Re-initializes the whole service configuration.
     * Can be used to release caches, re-login to the repository providers, etc.
     */
    public static void reload() {
        synchronized (LOCKOBJECT) {
            INSTANCE = new TemplateServiceConfiguration();
        }
    }

    /**
     * Initializes the service configuration with a custom config instance.
     * Can be used to override repository provider, etc. logic.
     * @param newConfiguration custom configuration
     * @throws TemplateServiceConfigurationException when invalid template caught.
     */
    public static void load(final TemplateServiceConfiguration newConfiguration)
            throws TemplateServiceConfigurationException {
        if (newConfiguration != null) {
            synchronized (LOCKOBJECT) {
                INSTANCE = newConfiguration;
            }
        } else {
            throw new TemplateServiceConfigurationException("876075ed-6e23-4d84-95ba-05f45ba9193a",
                    String.format("%s - trying to set the template service config to null.", MSG_INVALID_PARAMETERS) );
        }
    }

    /**
     * Returns the font basedir - @see converter.pdf.font-library.basedir.
     * @return String the font dir
     */
    public String getFontDir() {
        return fontDir;
    }

    /**
     * Sets the font dir for custom config.
     * @param fontDir the new font dir
     */
    protected void setFontDir(String fontDir) {
        this.fontDir = fontDir;
    }
}
