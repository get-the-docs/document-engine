package net.videki.templateutils.template.core.context.dto;

/*-
 * #%L
 * template-utils-core-dto-extensions
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
import net.videki.templateutils.template.core.context.ContextObjectProxyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface JsonModel {

  /**
   * Logger.
   */
  Logger LOGGER = LoggerFactory.getLogger(ContextObjectProxyBuilder.class);

  /**
   * JSON mapper.
   */
  ObjectMapper jsonModelMapper = new ObjectMapper();

  /**
   * Serializes the implementing object to json.
   *
   * @return the json string.
   */
  default String toJson() {
    try {
      return jsonModelMapper.writer().writeValueAsString(this);
    } catch (final JsonProcessingException e) {
      LOGGER.warn("Error serializing the object.", e);

      return null;
    }
  }
}
