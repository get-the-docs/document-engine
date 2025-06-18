package org.getthedocs.documentengine.core.processor.converter.pdf.docx4j;

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

import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.configuration.FontConfig;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.fonts.PhysicalFonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class for the pdf converter to register custom, non-default fonts to embedded in the pdf document.
 * @author Levente Ban
 */
public class AdditionalFontFinder {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalFontFinder.class);

    /**
     * Parses the specified font directory for the font mappings provided in the parameters.
     * @param fontConfigList the font mappings specified in the configuration (see document-engine.properties).
     * @throws TemplateServiceConfigurationException thrown if the font urls or the mapping is invalid.
     */
    public static void discoverFonts(final List<FontConfig> fontConfigList) throws TemplateServiceConfigurationException {
        if (fontConfigList == null) {
            throw new TemplateServiceConfigurationException("037e44ac-3dcf-4344-8989-31cf3fcfc624",
                    String.format("%s - font config null.", TemplateServiceConfigurationException.MSG_INVALID_PARAMETERS) );
        }

        for (Iterator<FontConfig> iter = fontConfigList.iterator(); iter.hasNext(); ) {
            try {
                final String baseDir = iter.next().getBasedir();
                if (StringUtils.isNotEmpty(baseDir)) {
                    PhysicalFonts.addPhysicalFont(new URL(baseDir));
                } else {
                    throw new TemplateServiceConfigurationException("656dca28-bc61-4059-991f-7bb65ec916e6",
                            String.format("%s - invalid font config url.", TemplateServiceConfigurationException.MSG_INVALID_PARAMETERS) );
                }
            } catch (final MalformedURLException e) {
                throw new TemplateServiceConfigurationException("656dca28-bc61-4059-991f-7bb65ec916e6",
                        String.format("%s - invalid font config url.", TemplateServiceConfigurationException.MSG_INVALID_PARAMETERS) );

            } catch (final Exception e) {
                LOGGER.warn("Error initializing document-engine config based additional fonts", e);
            }
        }
    }

}
