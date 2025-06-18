package org.getthedocs.documentengine.examples.singletemplatepojo;

import org.getthedocs.documentengine.examples.singletemplatepojo.dto.Person;
import org.getthedocs.documentengine.core.service.TemplateServiceRegistry;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceException;
import org.getthedocs.documentengine.core.util.FileSystemHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;

public class SingleTemplateDocxPojo {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleTemplateDocxPojo.class);

    public static void main(String[] args) {
        final SingleTemplateDocxPojo appService = new SingleTemplateDocxPojo();

        appService.generateDocs();
    }

    public void generateDocs() {
        final String inputDir = "hr-files";
        final String fileName = "personal_data_sheet_v1_3.docx";

        final var data = getTemplateData();

        try {
            TemplateServiceRegistry.getInstance().fillAndSave(
                    FileSystemHelper.getFileNameWithPath(inputDir, fileName),
                    data);

        } catch (final TemplateServiceException e) {
            LOGGER.error("Error generating the result doc.", e);
        }

    }

    private Person getTemplateData() {
        return new Person("John Doe",
                LocalDate.of(1970, Month.JULY, 20),
                LocalDate.of(2020, Month.NOVEMBER, 1));
    }
}
