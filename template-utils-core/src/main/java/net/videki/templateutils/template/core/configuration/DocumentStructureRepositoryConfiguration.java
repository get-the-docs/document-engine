package net.videki.templateutils.template.core.configuration;

import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureBuilder;

public class DocumentStructureRepositoryConfiguration extends RepositoryConfiguration {
    private DocumentStructureBuilder builder;

    public DocumentStructureRepositoryConfiguration(final DocumentStructureBuilder builder,
                                                    final String baseDir) {
        super(baseDir);
        this.builder = builder;
    }

    public DocumentStructureBuilder getBuilder() {
        return builder;
    }
}
