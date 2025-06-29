/*
 * Copyright (c) 2021-2021. Levente Ban
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

package org.getthedocs.documentengine.core.context.dto;

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

import org.getthedocs.documentengine.core.dto.ITemplate;
import org.getthedocs.documentengine.core.processor.input.PlaceholderEvalException;
import org.getthedocs.documentengine.core.context.JsonTemplateContext;
import org.getthedocs.documentengine.core.context.TemplateContext;

import java.time.LocalDate;

/**
 * Interface for JSON templates that provides a method to format date values.
 *
 * @author Levente Ban
 */
public interface IJsonTemplate extends ITemplate {

    /**
     * Formats a date value in a year-month-date format (YYYY-MM-DD) from the given JsonTemplateContext.
     *
     * @param value The JsonTemplateContext containing the date value.
     * @return The formatted date string, or a placeholder if the value is null.
     * @throws PlaceholderEvalException If an error occurs during date formatting.
     */
    default String fmtDate(final JsonTemplateContext value) {
        try {
            if (value != null) {
                final Integer year = (Integer) value.getValue(TemplateContext.CONTEXT_ROOT_KEY + "." + "year");
                final Integer month = (Integer) value.getValue(TemplateContext.CONTEXT_ROOT_KEY + "." + "month");
                final Integer day = (Integer) value.getValue(TemplateContext.CONTEXT_ROOT_KEY + "." + "day");
                return LocalDate.of(year, month, day).format(ITemplate.DATE_FORMAT_DATE);
            } else {
                return ITemplate.PLACEHOLDER_EMPTY;
            }
        } catch (final Exception e) {
            throw new PlaceholderEvalException("93868d56-af5b-4d31-b58e-f931a271dce7",
                    "Cannot convert data to date: " + value.toJson(), e);
        }
    }
}
