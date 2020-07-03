package net.videki.templateutils.template.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.provider.documentstructure.DocumentStructureRepository;
import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureBuilder;
import net.videki.templateutils.template.core.provider.documentstructure.builder.yaml.YmlDocStructureBuilder;
import net.videki.templateutils.template.core.provider.resultstore.ResultStore;
import net.videki.templateutils.template.core.provider.templaterepository.TemplateRepository;
import net.videki.templateutils.template.core.provider.templaterepository.filesystem.FileSystemTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.videki.templateutils.template.core.service.TemplateService;

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

    // template-utils.properties keys
    private static final String FONT_FAMILY = "converter.pdf.font-library.font";
    private static final String FONT_DIR = "converter.pdf.font-library.basedir";
    private static final String CONFIG_FILE_NAME = "template-utils.properties";
    private static final String LOG_APPENDER = "common.log.value-logcategory";

    private static final String DOCUMENT_STRUCTURE_PROVIDER = "repository.documentstructure.provider";
    private static final String DOCUMENT_STRUCTURE_PROVIDER_BASEDIR = "repository.documentstructure.provider.basedir";
    private static final String DOCUMENT_STRUCTURE_BUILDER = "repository.documentstructure.builder";
    private static final String TEMPLATE_REPOSITORY_PROVIDER = "repository.template.provider";
    private static final String TEMPLATE_REPOSITORY_PROVIDER_BASEDIR = "repository.template.provider.basedir";
    private static final String RESULT_REPOSITORY_PROVIDER = "repository.result.provider";
    private static final String RESULT_REPOSITORY_PROVIDER_BASEDIR = "repository.result.provider.basedir";

    private static final String PROPERTY_DELIMITER = ".";

    private static Properties properties;

    private DocumentStructureRepository documentStructureRepository;

    private TemplateRepository templateRepository;

    private ResultStore resultStore;

    private Map<String, Map<FontStyle, FontConfig>> styles = new TreeMap<>();

    TemplateServiceConfiguration() {
        init();
    }

    private void init() {

        properties = new Properties();
        Set<Object> keys = Collections.emptySet();
        try {
            properties.load(TemplateServiceConfiguration.getResource(CONFIG_FILE_NAME));
            keys = properties.keySet();
            if (!keys.isEmpty()) {
                LOGGER.info("template-utils.properties configuration file found.");
            }
        } catch (Exception e) {
            LOGGER.warn("template-utils.properties configuration file not found, using default configuration.");
        }
        initFontLibrary(keys);
        initDocumentStructureRepository();
        initTemplateRepository();
        initResultStore();
    }

    private void initFontLibrary(Set<Object> keys) {
        if (keys != null && !keys.isEmpty()) {
            final String basedir = (String) properties.get(FONT_DIR);

            for (Object actKey : keys) {
                final String s = (String) actKey;
                if (s != null && s.startsWith(FONT_FAMILY)) {

                    final String[] parts = s.split(".");

                    if (parts.length == 5) {
                        final String actFamily = parts[4];

                        for (final FontStyle fs : FontStyle.values()) {
                            final String fileForStyle = (String) properties.get(s + PROPERTY_DELIMITER + fs);
                            final FontConfig f = new FontConfig();
                            f.setFontFamily(actFamily);
                            f.setStyle(fs);
                            f.setBasedir(basedir);
                            f.setFileName(fileForStyle);

                            Map<FontStyle, FontConfig> fm = styles.get(actFamily);
                            if (fm == null) {
                                fm = new TreeMap<>();
                                styles.put(actFamily, fm);
                            }
                            fm.put(fs, f);
                        }
                    }
                }
            }
        }
    }

    private void initDocumentStructureRepository() {
        String repositoryProvider = "<Not configured or could not read properties file>";
        try {
            repositoryProvider = (String) properties.get(DOCUMENT_STRUCTURE_PROVIDER);

            if (repositoryProvider != null) {
                final DocumentStructureRepository tmpRepo = (DocumentStructureRepository)
                        this.getClass().getClassLoader()
                                .loadClass(repositoryProvider)
                                .getDeclaredConstructor()
                                .newInstance();

                final String repositoryBaseDir = (String) properties.get(DOCUMENT_STRUCTURE_PROVIDER_BASEDIR);
                final DocumentStructureRepositoryConfiguration drc =
                        new DocumentStructureRepositoryConfiguration(loadDocumentStructureBuilder(),
                                repositoryBaseDir);
                tmpRepo.init(drc);

                this.documentStructureRepository = tmpRepo;
            } else {
                final String msg = String.format("Document structure repository not specified, " +
                        "using fallback built-in FileSystemTemplateRepository.");
                LOGGER.info(msg);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            final String msg = String.format("Error loading document structure repository: %s, " +
                    "using fallback built-in FileSystemTemplateRepository.", repositoryProvider);
            LOGGER.error(msg, e);

            this.templateRepository = new FileSystemTemplateRepository();
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private DocumentStructureBuilder loadDocumentStructureBuilder() {
        DocumentStructureBuilder documentStructureBuilder = new YmlDocStructureBuilder();

        String repositoryProvider = "<Not configured or could not read properties file>";
        try {
            repositoryProvider = (String) properties.get(DOCUMENT_STRUCTURE_BUILDER);

            if (repositoryProvider != null) {
                documentStructureBuilder = (DocumentStructureBuilder)
                        this.getClass().getClassLoader()
                                .loadClass(repositoryProvider)
                                .getDeclaredConstructor()
                                .newInstance();
            } else {
                final String msg = String.format("Document structure builder not specified, " +
                        "using built-in YmlDocStructureBuilder.");
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

    private void initTemplateRepository() {
        String repositoryProvider = "<Not configured or could not read properties file>";
        try {
            repositoryProvider = (String) properties.get(TEMPLATE_REPOSITORY_PROVIDER);

            if (repositoryProvider != null) {
                final TemplateRepository tmpRepo = (TemplateRepository)
                        this.getClass().getClassLoader()
                                .loadClass(repositoryProvider)
                                .getDeclaredConstructor()
                                .newInstance();

                final String repositoryBaseDir = (String) properties.get(TEMPLATE_REPOSITORY_PROVIDER_BASEDIR);
                final RepositoryConfiguration rc =
                        new RepositoryConfiguration(repositoryBaseDir);
                tmpRepo.init(rc);

                this.templateRepository = tmpRepo;
            } else {
                final String msg = String.format("Template repository not specified, " +
                        "using fallback built-in FileSystemTemplateRepository.");
                LOGGER.info(msg);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException |
                NoSuchMethodException | InvocationTargetException e) {
            final String msg = String.format("Error loading template repository: %s, " +
                    "using fallback built-in FileSystemTemplateRepository.", repositoryProvider);
            LOGGER.error(msg, e);

            this.templateRepository = new FileSystemTemplateRepository();
        }
    }

    private void initResultStore() {
        String repositoryProvider = "<Not configured or could not read properties file>";
        try {
            repositoryProvider = (String) properties.get(RESULT_REPOSITORY_PROVIDER);

            if (repositoryProvider != null) {
                ResultStore tmpRepo = (ResultStore)
                        this.getClass().getClassLoader()
                                .loadClass(repositoryProvider)
                                .getDeclaredConstructor()
                                .newInstance();

                final String repositoryBaseDir = (String) properties.get(RESULT_REPOSITORY_PROVIDER_BASEDIR);
                final RepositoryConfiguration rc =
                        new RepositoryConfiguration(repositoryBaseDir);
                tmpRepo.init(rc);

                this.resultStore = tmpRepo;
            } else {
                final String msg = String.format("Template result repository not specified, " +
                        "using fallback built-in FileSystemTemplateRepository.");
                LOGGER.info(msg);

            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException |
                NoSuchMethodException | InvocationTargetException e) {
            final String msg = String.format("Error loading result store repository: %s, " +
                    "using fallback built-in FilesystemResultStore.", repositoryProvider);
            LOGGER.error(msg, e);
        }
    }

    private static InputStream getResource(String filename) throws java.io.IOException {
        java.net.URL url = TemplateServiceConfiguration.class.getClassLoader().getResource(filename);
/*
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(filename);
        }
*/
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

    public String getDocStructureLogCategory() {
        return this.properties.getProperty(LOG_APPENDER);
    }

    public DocumentStructureRepository getDocumentStructureRepository() {
        return documentStructureRepository;
    }

    public TemplateRepository getTemplateRepository() {
        return this.templateRepository;
    }

    public ResultStore getResultStore() {
        return this.resultStore;
    }

    public static TemplateServiceConfiguration getInstance() {
        TemplateServiceConfiguration result = INSTANCE;
        if (result == null) {
            synchronized (INSTANCE) {
                result = INSTANCE = new TemplateServiceConfiguration();
            }
        }
        if (result == null) {
            LOGGER.error("Could not create TemplateServiceConfiguration.");
        }
        return result;
    }

    /**
     * Re-initializes the whole service configuration.
     * Can be used to relase caches, re-login to the repository providers, etc.
     */
    public static void reload() {
        synchronized (INSTANCE) {
            INSTANCE = new TemplateServiceConfiguration();
        }
    }

    public Properties getConfigurationProperties() {
        return properties;
    }
}
