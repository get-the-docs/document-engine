package net.videki.templateutils.template.test.dto;

import net.videki.templateutils.template.test.dto.doc.DocumentProperties;

import java.time.LocalDateTime;

public class DocDataFactory {
    public static DocumentProperties createDocData(final String transactionId) {
        final DocumentProperties result = new DocumentProperties();

        result.setLogin("PB\\cnorris");
        result.setGenerationDate(LocalDateTime.now());
        result.setDmsUrl("http://dms.internal.pbvintage.com/" + transactionId);

        return result;
    }

}
