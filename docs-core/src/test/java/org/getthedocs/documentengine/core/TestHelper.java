package org.getthedocs.documentengine.core;

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

import org.getthedocs.documentengine.core.documentstructure.GenerationResult;
import org.getthedocs.documentengine.core.documentstructure.ResultDocument;
import org.getthedocs.documentengine.core.service.TemplateServiceParamTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceParamTest.class);

    public static void closeResults(GenerationResult result) {
        if (result != null) {
            for (final ResultDocument as : result.getResults()) {
                try {
                    if (as != null && as.getContent() != null) {
                        as.getContent().close();
                    }
                } catch (IOException e) {
                    final String msg = "Error closing result stream.";
                    LOGGER.error(msg);
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug(msg, e);
                    }

                }
            }
        }
    }

}
