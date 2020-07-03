package net.videki.templateutils.template.core.configuration;

public class RepositoryConfiguration {
    private String baseDir;

    public RepositoryConfiguration(final String baseDir) {
        this.baseDir = baseDir;
    }

    public String getBaseDir() {
        return baseDir;
    }

}
