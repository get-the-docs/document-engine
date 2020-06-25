package net.videki.templateutils.template.core.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import net.videki.templateutils.template.core.provider.resultstore.ResultStore;
import net.videki.templateutils.template.core.provider.templaterepository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.videki.templateutils.template.core.service.TemplateService;

public class TemplateServiceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

    private static TemplateServiceConfiguration INSTANCE = new  TemplateServiceConfiguration();

    // template-utils.properties keys
    private static final String FONT_FAMILY = "converter.pdf.font-library.font";
    private static final String FONT_DIR = "converter.pdf.font-library.basedir";
    private static final String CONFIG_FILE_NAME = "template-utils.properties";
    private static final String LOG_APPENDER = "common.log.value-logcategory";
    private static final String TEMPLATE_REPOSITORY_PROVIDER = "repository.template.provider";
    private static final String RESULT_REPOSITORY_PROVIDER = "repository.result.provider";

    private static final String PROPERTY_DELIMITER = ".";

    private static Properties properties;

    private TemplateRepository templateRepository;

    private ResultStore resultStore;

    private Map<String, Map<FontStyle, FontConfig>> styles = new TreeMap<>();

    TemplateServiceConfiguration() {
        init();
    }

    private void init() {

        properties = new Properties();
        try {
            properties.load(TemplateServiceConfiguration.getResource(File.separator + CONFIG_FILE_NAME));

            Set<Object> keys = properties.keySet();

            initTemplateRepository();
            initResultStore();
            initFontLibrary(keys);

        } catch (Exception e) {
            LOGGER.warn("Cannot read template-utils.properties; " + e.getMessage());
        }
    }

    private void initFontLibrary(Set<Object> keys) {
        final String basedir = (String) properties.get(FONT_DIR);
        for (Object actKey : keys) {
            final String s = (String) actKey;
            if (s != null && s.startsWith(FONT_FAMILY)) {

                final String[] parts = s.split(".");
                final String actFamily = parts[2];

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

    private void initTemplateRepository() {
        final String repositoryProvider = (String) properties.get(TEMPLATE_REPOSITORY_PROVIDER);

        try {
            this.templateRepository = (TemplateRepository)
                    this.getClass().getClassLoader().loadClass(repositoryProvider).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            final String msg = String.format("Error loading template repository: %s, " +
                    "using fallback built-in FileSystemTemplateRepository.", repositoryProvider);
            LOGGER.error(msg, e);
        }
    }

    private void initResultStore() {
        final String repositoryProvider = (String) properties.get(RESULT_REPOSITORY_PROVIDER);

        try {
            this.resultStore = (ResultStore)
                    this.getClass().getClassLoader().loadClass(repositoryProvider).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            final String msg = String.format("Error loading result store repository: %s, " +
                    "using fallback built-in FilesystemResultStore.", repositoryProvider);
            LOGGER.error(msg, e);
        }
    }

    private static InputStream getResource(String filename) throws java.io.IOException {
        java.net.URL url = TemplateServiceConfiguration.class.getClassLoader().getResource(filename);

        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(filename);
        }

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
        return result;
    }

    public static void reload() {
        synchronized (INSTANCE) {
            INSTANCE = new TemplateServiceConfiguration();
        }
    }

    public Properties getConfigurationProperties() {
        return properties;
    }
}
