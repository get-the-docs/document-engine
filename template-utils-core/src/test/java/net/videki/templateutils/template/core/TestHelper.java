package net.videki.templateutils.template.core;

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

import net.videki.templateutils.template.core.context.dto.TemplateContext;
import net.videki.templateutils.template.core.documentstructure.ResultDocument;
import net.videki.templateutils.template.core.documentstructure.GenerationResult;
import net.videki.templateutils.template.core.documentstructure.StoredResultDocument;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
import net.videki.templateutils.template.core.service.TemplateServiceParamTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceParamTest.class);

    public static final String JSON_DIR = "/values/integrationtests";

    public static String getTestDataFromFile(final String fileName) {

        String result = "";

        try {
            final File file = new ClassPathResource(fileName).getFile();
            final BufferedReader br = new BufferedReader(new FileReader(file));

            String actLine;
            final StringBuilder tmp = new StringBuilder();
            while ((actLine = br.readLine()) != null) {
                tmp.append(actLine);
            }

            result = tmp.toString();
        } catch (final IOException e) {
            LOGGER.warn("Could not read test data file: {}", e.getMessage());
        }

        return result;
    }

    protected String getDataForTestCase(final String fileName) {
        return getTestDataFromFile(JSON_DIR + File.separator +
                this.getClass().getSimpleName() + File.separator +
                new Throwable().getStackTrace()[1].getMethodName() + File.separator + fileName);
    }

    protected ValueSet getValueSetForTestCase(final String fileName) {
        final ValueSet result = new ValueSet();

        final String valuesInJson = getTestDataFromFile(JSON_DIR + File.separator +
                this.getClass().getSimpleName() + File.separator +
                new Throwable().getStackTrace()[1].getMethodName() + File.separator + fileName);

        result.withContext(valuesInJson);

        return result;
    }

    public static void closeResults(GenerationResult result) {
        if (result != null) {
            for (final ResultDocument as : result.getResults()) {
                try {
                    if (as != null && as.getContent() != null) {
                        as.getContent().close();
                    }
                } catch (IOException e) {
                    LOGGER.error("Error closing result stream.", e);
                }
            }
        }
    }


    public static boolean isDocumentFilled(final StoredResultDocument resultDocument) {
        return true;
    }

    public static boolean isDocumentFilled(final List<StoredResultDocument> resultDocuments) {
        if (resultDocuments != null) {
            return resultDocuments.stream().allMatch(TestHelper::isDocumentFilled);
        } else {
            return true;
        }
    }
}
