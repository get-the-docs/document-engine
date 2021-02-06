package net.videki.templateutils.documentstructure.builder.core.documentstructure;

import net.videki.templateutils.documentstructure.builder.core.service.DocumentStructureOptionsBuilder;
import net.videki.templateutils.documentstructure.builder.core.service.impl.YmlDocStructureBuilder;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;
import net.videki.templateutils.template.core.service.exception.TemplateNotFoundException;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.fail;

public class DocumentStructureOptionsTest {

    @Test
    public void readDocStructure() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlDocStructureBuilder();

            final DocumentStructureOptions dso = dsBuilder.buildOptions("/contract_v02-options.yml");

        } catch (TemplateNotFoundException | TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void docStructureOptionsCheckFriendlyNameOk() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlDocStructureBuilder();

            final DocumentStructureOptions dso = dsBuilder.buildOptions("/contract_v02-options.yml");

            Assert.assertTrue(dso.getElements() != null && !dso.getElements().isEmpty() &&
                    dso.getElementIdByFriendlyName("conditions-vintage_basic_underaged").isPresent());
        } catch (TemplateNotFoundException | TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void docStructureOptionsCheckOptionalTemplateElementOk() {
        try {
            final DocumentStructureOptionsBuilder dsBuilder = new YmlDocStructureBuilder();

            final DocumentStructureOptions dso = dsBuilder.buildOptions("/contract_v02-options.yml");

            Assert.assertNotNull(dso.getElements().get(0));

        } catch (TemplateNotFoundException | TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void docStructureOptionsReplaceElementWithOptionOk() {
        try {
            final YmlDocStructureBuilder dsBuilder = new YmlDocStructureBuilder();

            final DocumentStructure ds = dsBuilder.build("/contract-vintage_v02.yml");
            final DocumentStructureOptions dso = dsBuilder.buildOptions("/contract_v02-options.yml");

            final Optional<TemplateElement> baseElement = ds.getElementByFriendlyName("conditions");
            final Optional<OptionalTemplateElement> option =
                    dso.getElementByFriendlyName("conditions-vintage_basic_underaged");
            if (baseElement.isPresent() && option.isPresent()) {
                final TemplateElement baseElementData = baseElement.get();
                final OptionalTemplateElement optionData = option.get();
                optionData.applyElement(baseElement.get());

                Assert.assertEquals("conditions-vintage_basic_underaged",
                        baseElementData.getTemplateElementId().getId());
            } else {
                fail();
            }

        } catch (TemplateNotFoundException | TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void docStructureOptionsReplaceElementWithOptionAsTemplateElementOk() {
        try {
            final YmlDocStructureBuilder dsBuilder = new YmlDocStructureBuilder();

            final DocumentStructure ds = dsBuilder.build("/contract-vintage_v02.yml");
            final DocumentStructureOptions dso = dsBuilder.buildOptions("/contract_v02-options.yml");

            final Optional<TemplateElement> baseElement = ds.getElementByFriendlyName("conditions");
            final Optional<OptionalTemplateElement> option =
                    dso.getElementByFriendlyName("conditions-vintage_basic_underaged");
            if (baseElement.isPresent() && option.isPresent()) {
                final TemplateElement baseElementData = baseElement.get();
                final OptionalTemplateElement optionData = option.get();
                optionData.applyElement(baseElement.get());

                Assert.assertEquals("conditions-vintage_basic_underaged",
                        optionData.asTemplateElement().getTemplateElementId().getId());

            } else {
                fail();
            }

        } catch (TemplateNotFoundException | TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void docStructureOptionsGetElementOption() {
        try {
            final YmlDocStructureBuilder dsBuilder = new YmlDocStructureBuilder();

            final DocumentStructure ds = dsBuilder.build("/contract-vintage_v02.yml");
            final DocumentStructureOptions dso = dsBuilder.buildOptions("/contract_v02-options.yml");

            final Optional<TemplateElement> baseElement = ds.getElementByFriendlyName("conditions");
            final Optional<OptionalTemplateElement> option =
                    dso.getElementByFriendlyName("conditions-vintage_basic_underaged");
            if (baseElement.isPresent() && option.isPresent()) {
                final OptionalTemplateElement optionData = option.get();
                optionData.applyElement(baseElement.get());

                Assert.assertEquals(TemplateElementOption.REPLACE, optionData.getOption());

            } else {
                fail();
            }

        } catch (TemplateNotFoundException | TemplateServiceException e) {
            e.printStackTrace();
            fail();
        }
    }
}
