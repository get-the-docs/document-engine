package net.videki.templateutils.template.core.util;


import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileSystemHelper {
    public static final String FILENAME_COLON = ".";

    public static InputStream getInputStream(final OutputStream out) {
        return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());

    }

    public static List<InputStream> getInputStream(final List<OutputStream> out) {
        List<InputStream> results = new ArrayList<>(out.size());
        for (OutputStream o : out) {
            results.add(getInputStream(o));
        }
        return results;

    }

    public static OutputStream getOutputStream() {
        return new ByteArrayOutputStream();
    }

    public static String getFullPath(final String path, final String fileName) {
        return path + File.separator + fileName;
    }

    public static String getFileName(final String fileName) {
        if (fileName != null) {
            final String result = Paths.get(fileName).getFileName().toString();

            return result;
        }
        return "";
    }
}
