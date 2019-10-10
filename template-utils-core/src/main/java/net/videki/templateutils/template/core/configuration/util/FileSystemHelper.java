package net.videki.templateutils.template.core.configuration.util;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileSystemHelper {
    public static final String            FILENAME_COLON = ".";
    public static final String            FILENAME_DIR_SEPARATOR = "/";

    public static InputStream getInputStream(@NotNull final OutputStream out) {
        return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());

    }

    public static List<InputStream> getInputStream(@NotNull final List<OutputStream> out) {
        List<InputStream> results = new ArrayList<>(out.size());
        for (OutputStream o : out) {
            results.add(getInputStream(o));
        }
        return results;

    }

    public static OutputStream getOutputStream() {
        return new ByteArrayOutputStream();
    }
}
