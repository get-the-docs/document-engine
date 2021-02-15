package net.videki.templateutils.documentstructure.builder.core.documentstructure.repository.filesystem;

import net.videki.templateutils.template.core.configuration.TemplateServiceConfiguration;
import net.videki.templateutils.template.core.provider.documentstructure.builder.DocumentStructureOptionsBuilder;
import net.videki.templateutils.template.core.provider.documentstructure.builder.yaml.YmlConfigurableDocStructureBuilder;
import net.videki.templateutils.template.core.provider.documentstructure.repository.ConfigurableDocumentStructureRepository;
import net.videki.templateutils.template.core.provider.documentstructure.repository.filesystem.FileSystemConfigurableDocumentStructureRepository;
import net.videki.templateutils.template.core.provider.documentstructure.repository.filesystem.FileSystemDocumentStructureRepository;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.fail;

public class FileSystemConfigurableDocumentStructureRepositoryTest {

    @Test
    public void getDocumentStructureOptions() {
        try {
            final var optionsRepo =
                    (ConfigurableDocumentStructureRepository) TemplateServiceConfiguration
                    .getInstance()
                    .getDocumentStructureRepository();
            final var dso =
                    optionsRepo.getDocumentStructureOptions("contracts/contract_v02-options.yml");

            Assert.assertNotNull(dso);
        } catch (final TemplateNotFoundException | TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void getDocumentStructureOptionsNonExisting() {
        try {
            final var optionsRepo =
                    (ConfigurableDocumentStructureRepository) TemplateServiceConfiguration
                            .getInstance()
                            .getDocumentStructureRepository();
            final var dso =
                    optionsRepo.getDocumentStructureOptions("contracts/contract_v02-options-there_is_no_such_file.yml");

        } catch (final TemplateProcessException e) {
            Assert.assertEquals("0578f1ec-a33b-4a73-af71-a14f0e55c0b9", e.getCode());

        } catch (final TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }

    }

}