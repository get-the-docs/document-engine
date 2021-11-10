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

package net.videki.templateutils.template.core.service;

import net.videki.templateutils.template.core.util.FileSystemHelper;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;

/**
 * Supported input formats.
 * 
 * @author Levente Ban
 */
public enum InputFormat {

  /**
   * Docx format.
   */
  DOCX("DOCX"),

  /**
   * xlsx format.
   */
  XLSX("XLSX");

  /**
   * The format string.
   */
  private final String strValue;

  /**
   * Constructor with the format string.
   * 
   * @param pValue the format.
   */
  InputFormat(final String pValue) {
    this.strValue = pValue;
  }

  /**
   * Returns the format.
   * 
   * @return the format.
   */
  public String getStrValue() {
    return strValue;
  }

  /**
   * Checks a format to check that they have the same format.
   * 
   * @param a the format to check.
   * @return true if the parameter has the same format.
   */
  public boolean isSameFormat(final Object a) {
    if (a != null) {
      if (a instanceof OutputFormat) {
        return this.name().equals(((OutputFormat) a).name());
      }
    }
    return false;
  }

  /**
   * Tries to determine the the input format from a file name by extension.
   * 
   * @param templateName the template name (in form of a file name).
   * @return the input format, if it is supported.
   * @throws IllegalArgumentException thrown in case of unhandled or
   *                                  indeterminable format.
   */
  public static InputFormat getInputFormatForFileName(final String templateName) {
    InputFormat format;
    try {
      if (templateName == null) {
        return null;
      }

      int fileExtPos = templateName.lastIndexOf(FileSystemHelper.FILENAME_COLON);
      if (fileExtPos > 0) {
        format = InputFormat
            .valueOf(templateName.toUpperCase().substring(fileExtPos).replace(FileSystemHelper.FILENAME_COLON, ""));

      } else {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException e) {
      final String msg = String.format(
          "Unhandled template file format " + "(input processor for the filename extension not found). Filename: %s",
          templateName);
      throw new TemplateProcessException("c14d63df-8db2-45a2-bf21-e62fe60a23a0", msg);
    }

    return format;
  }

}
