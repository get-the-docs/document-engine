package net.videki.templateutils.template.core.util;


import java.io.*;
import java.nio.file.Paths;

public class FileSystemHelper {
    public static final String FILENAME_COLON = ".";

    public static InputStream getInputStream(final OutputStream out) {
        return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());

    }

    public static OutputStream getOutputStream() {
        return new ByteArrayOutputStream();
    }

    public static String getFileNameWithPath(final String path, final String fileName) {
        return path + File.separator + fileName;
    }

    public static String getFileName(final String fileName) {
        if (fileName != null) {

            return Paths.get(fileName).getFileName().toString();
        }
        return "";
    }
}
