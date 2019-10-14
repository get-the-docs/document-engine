package net.videki.templateutils.template.core.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileSystemHelper {
    public static final String            FILENAME_COLON = ".";
    public static final String            FILENAME_DIR_SEPARATOR = "/";

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
        return path + FILENAME_DIR_SEPARATOR + fileName;
    }

    public static String getFileName(final String fileName) {
        if (fileName != null) {
            int lastPos = fileName.lastIndexOf(FILENAME_DIR_SEPARATOR);
            final String result = fileName.substring(lastPos).replace(FILENAME_DIR_SEPARATOR, "");

            return result;
        }
        return "";
    }
}
