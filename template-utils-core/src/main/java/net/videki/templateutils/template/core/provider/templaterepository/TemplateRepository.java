package net.videki.templateutils.template.core.provider.templaterepository;

import java.io.InputStream;

public interface TemplateRepository {

    InputStream getTemplate(final String templateFile);

}
