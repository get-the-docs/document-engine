package net.videki.templateutils.template.core.provider.resultstore.filesystem;

import net.videki.templateutils.template.core.documentstructure.ResultDocument;
import net.videki.templateutils.template.core.documentstructure.GenerationResult;
import net.videki.templateutils.template.core.provider.resultstore.ResultStore;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileSystemResultStoreTest {

    @Test
    public void saveNullInputDocumentResultTest() {
        final ResultStore resultStore = new FileSystemResultStore();

        try {
            resultStore.save((ResultDocument) null);
        } catch (final TemplateProcessException e) {
            if (!"cb05a816-dea2-4498-823a-33fb4ece1565".equals(e.getCode())) {
                fail();
            }

        }
    }

    @Test
    public void saveNullInputGenerationResultTest() {
        final ResultStore resultStore = new FileSystemResultStore();

        try {
            resultStore.save((GenerationResult) null);
        } catch (final TemplateProcessException e) {
            if (!"0d383b14-c3ef-430d-9007-3add27e086d8".equals(e.getCode())) {
                fail();
            }

        }
    }
}