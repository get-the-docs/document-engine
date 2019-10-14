package net.videki.templateutils.template.test.dto;

import net.videki.templateutils.template.test.dto.doc.DocumentProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DocDataFactory {
    public static DocumentProperties createDocData() {
        final DocumentProperties result = new DocumentProperties();

        result.setLogin("PB\\cnorris");
        result.setGenerationDate(LocalDateTime.now());

        return result;
    }

}
