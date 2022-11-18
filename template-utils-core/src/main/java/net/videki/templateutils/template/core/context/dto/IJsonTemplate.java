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

package net.videki.templateutils.template.core.context.dto;

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

import net.videki.templateutils.template.core.context.JsonTemplateContext;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.dto.ITemplate;
import net.videki.templateutils.template.core.processor.input.PlaceholderEvalException;

import java.time.LocalDate;

/**
 * @author Levente Ban
 */
public interface IJsonTemplate extends ITemplate {

    default String fmtDate(final JsonTemplateContext value) {
        try {
            String result;
            if (value != null) {
                final Integer year = (Integer) value.getValue(TemplateContext.CONTEXT_ROOT_KEY + "." + "year");
                final Integer month = (Integer) value.getValue(TemplateContext.CONTEXT_ROOT_KEY + "." + "month");
                final Integer day = (Integer) value.getValue(TemplateContext.CONTEXT_ROOT_KEY + "." + "day");
                result = LocalDate.of(year, month, day).format(ITemplate.DATE_FORMAT_DATE);;
            } else {
                result = ITemplate.PLACEHOLDER_EMPTY;
            }
            return result;
        } catch (final Exception e) {
            throw new PlaceholderEvalException("93868d56-af5b-4d31-b58e-f931a271dce7",
                    "Cannot convert data to date: " + value.toJson(), e);
        }
    }
}
