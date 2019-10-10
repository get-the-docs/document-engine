package net.videki.templateutils.template.core.processor.merger;

import net.videki.templateutils.template.core.service.OutputFormat;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface OutputMerger {

    OutputFormat getOutputFormat();
    OutputStream merge(@NotNull final List<InputStream> sources);
}
