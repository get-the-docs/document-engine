package net.videki.documentengine.core.configuration;

/*-
 * #%L
 * docs-core
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
 * Configurable font styles.
 *
 * @see FontConfig
 *
 * @author Levente Ban
 */
public enum FontStyle {

    /**
     * Font style: bold.
     */
    BOLD("bold"),

    /**
     * Font style: bold italic.
     */
    BOLD_ITALIC("boldItalic"),

    /**
     * Font style: italic.
     */
    ITALIC("italic"),

    /**
     * Font style: normal.
     */
    NORMAL("normal");

    private String value;

    /**
     * Font style with the given style.
     * @param value the font style.
     */
    FontStyle(String value) {
        this.value = value;
    }

    /**
     * Returns the font style. 
     * @return the font style.
     */
    public String getValue() {
        return value;
    }

}
