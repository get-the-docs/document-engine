package org.getthedocs.documentengine.core.dto;

/*-
 * #%L
 * docs-core-dto
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.getthedocs.documentengine.core.dto.json.ObjectMapperFactory;

public interface JsonModel {

  default String toJson() {

    final ObjectMapper mapper = ObjectMapperFactory.jsonMapper();
    mapper.registerModule(new JavaTimeModule());

    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
