package net.videki.templateutils.template.core.provider.templaterepository;

import net.videki.templateutils.template.core.configuration.RepositoryConfiguration;

import java.io.InputStream;

public interface TemplateRepository {

    void init(RepositoryConfiguration props);

    InputStream getTemplate(final String templateFile);

}
