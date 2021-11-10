/*
 * Copyright (c) 2021. Levente Ban
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
