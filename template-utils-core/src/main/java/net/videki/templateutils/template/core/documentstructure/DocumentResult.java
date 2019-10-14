package net.videki.templateutils.template.core.documentstructure;

import net.videki.templateutils.template.core.documentstructure.descriptors.GenerationResultMode;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElement;
import net.videki.templateutils.template.core.documentstructure.descriptors.TemplateElementId;
import net.videki.templateutils.template.core.service.OutputFormat;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Levente Ban
 *
 * <p>If multiple documents have to be generated, the DocumentStructure contains
 * the template file name and its binary.</p>
 * <p></p>
 */
public class DocumentResult {
    /**
     * The document binary
     */
    private final OutputStream content;
    /**
     * The result's filename
     */
    private final String fileName;

    public DocumentResult(final String fileName, final OutputStream content) {
        this.fileName = fileName;
        this.content = content;
    }

    public OutputStream getContent() {
        return content;
    }


    public String getFileName() {
        return fileName;
    }

}
