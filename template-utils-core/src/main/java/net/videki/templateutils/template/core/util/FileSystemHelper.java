package net.videki.templateutils.template.core.util;

import java.io.*;
import java.nio.file.Paths;

/**
 * Helper class for file system operations.
 * 
 * @author Levente Ban
 */
public class FileSystemHelper {

    /**
     * Filename separator.
     */
    public static final String FILENAME_COLON = ".";

    /**
     * Returns an input stream for an output stream. (for further processing)
     * 
     * @param out the outputstream
     * @return the inputstream
     */
    public static InputStream getInputStream(final OutputStream out) {
        return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());

    }

    /**
     * Returns a new outputstream.
     * 
     * @return the outputstream.
     */
    public static OutputStream getOutputStream() {
        return new ByteArrayOutputStream();
    }

    /**
     * Returns a path string for an object.
     * 
     * @param path     the object path.
     * @param fileName the filename.
     * @return the path as string.
     */
    public static String getFileNameWithPath(final String path, final String fileName) {
        var tmpPath = path;
        if (tmpPath.endsWith("\\") || tmpPath.endsWith("/")) {
            tmpPath = tmpPath.substring(0, tmpPath.length() - 1);
        }
        return Paths.get(tmpPath + File.separator + fileName).normalize().toUri().getPath();
    }

    /**
     * Returns the filename for a path-like string.
     * 
     * @param fileName the path.
     * @return the filename.
     */
    public static String getFileName(final String fileName) {
        if (fileName != null) {

            return Paths.get(fileName).getFileName().toString();
        }
        return "";
    }
}
