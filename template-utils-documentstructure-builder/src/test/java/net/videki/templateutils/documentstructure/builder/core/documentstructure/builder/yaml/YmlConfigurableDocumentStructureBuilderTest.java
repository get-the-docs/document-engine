package net.videki.templateutils.documentstructure.builder.core.documentstructure.builder.yaml;

import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureOptionsBuilder;
import net.videki.templateutils.template.core.provider.documentstructure.builder.yaml.YmlConfigurableDocStructureBuilder;
import net.videki.templateutils.template.core.provider.documentstructure.repository.filesystem.FileSystemDocumentStructureRepository;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.fail;

public class YmlConfigurableDocumentStructureBuilderTest {

    @Test
    public void readDocStructureOptions() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlConfigurableDocStructureBuilder();

            final InputStream dsOptionsFileAsStream = FileSystemDocumentStructureRepository
                    .class.getClassLoader().getResourceAsStream("documentstructures/contracts/contract_v02-options.yml");
            dsBuilder.buildOptions(dsOptionsFileAsStream);

        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }

    }

}