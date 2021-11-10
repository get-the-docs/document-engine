package net.videki.templateutils.template.core.service;

/*-
 * #%L
 * template-utils-core
 * %%
 * Copyright (C) 2021 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Supported output formats.
 * 
 * @author Levente Ban
 */
public enum OutputFormat {

    // RTF,

    /**
     * Wildcard format to mark that the document format should not be changed during
     * generation.
     */
    UNCHANGED,

    /**
     * Docx format.
     */
    DOCX,

    /**
     * Pdf.
     */
    PDF;

    /**
     * Checks a format to check that they have the same format.
     * 
     * @param a the format to check.
     * @return true if the parameter has the same format.
     */
    public boolean isSameFormat(final Object a) {
        if (a != null) {
            if (UNCHANGED.name().equals(this.name())) {
                return true;
            } else if ((a instanceof InputFormat)) {
                return ((InputFormat) a).name().equals(this.name());
            }
        }
        return false;
    }
}
