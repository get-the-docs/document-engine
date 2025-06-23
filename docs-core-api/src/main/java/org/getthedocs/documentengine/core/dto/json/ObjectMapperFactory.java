package org.getthedocs.documentengine.core.dto.json;

/*-
 * #%L
 * docs-core-dto
 * %%
 * Copyright (C) 2023 - 2025 Levente Ban
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Factory class for creating {@link ObjectMapper} instances with pre-configured settings to use
 * coherent settings across the application.
 * Provides methods for both JSON and YAML mappers, with options to register additional modules.
 */
public class ObjectMapperFactory {

    /**
     * Creates a new {@link ObjectMapper} instance configured for JSON serialization/deserialization.
     *
     * @return a new {@link ObjectMapper} instance for JSON.
     */
    public static ObjectMapper jsonMapper() {
        return getMapper(null);
    }

    /**
     * Creates a new {@link ObjectMapper} instance configured for JSON serialization/deserialization,
     * with an additional module registered. Use this method to register custom serializers or deserializers.
     *
     * @param additionalModule the additional module to register with the mapper.
     * @return a new {@link ObjectMapper} instance for JSON with the additional module registered.
     */
    public static ObjectMapper jsonMapper(final SimpleModule additionalModule) {
        final ObjectMapper mapper = getMapper(null);
        mapper.registerModule(additionalModule);

        return mapper;
    }

    /**
     * Creates a new {@link ObjectMapper} instance configured for YAML serialization/deserialization.
     *
     * @return a new {@link ObjectMapper} instance for YAML.
     */
    public static ObjectMapper yamlMapper() {
        return getMapper(new YAMLFactory());
    }

    /**
     * Creates a new {@link ObjectMapper} instance configured for YAML serialization/deserialization,
     * with an additional module registered. Use this method to register custom serializers or deserializers.
     *
     * @param additionalModule the additional module to register with the mapper.
     * @return a new {@link ObjectMapper} instance for YAML with the additional module registered.
     */
    public static ObjectMapper yamlMapper(final SimpleModule additionalModule) {
        final ObjectMapper mapper = getMapper(new YAMLFactory());
        mapper.registerModule(additionalModule);

        return mapper;
    }

    /**
     * Internal method to create a new {@link ObjectMapper} instance with the specified {@link JsonFactory}.
     * Configures the mapper with various serialization/deserialization settings.
     *
      * @param jsonFactory the {@link JsonFactory} to use for creating the mapper.
     * @return a new {@link ObjectMapper} instance configured with the specified {@link JsonFactory}.
     */
    private static ObjectMapper getMapper(final JsonFactory jsonFactory) {
        final ObjectMapper mapper = jsonFactory == null ? new ObjectMapper() : new ObjectMapper(jsonFactory);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configOverride(List.class).setSetterInfo(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        final OffsetDateTimeSerializer offsetDateTimeSerializer = OffsetDateTimeSerializer.INSTANCE;
        javaTimeModule.addSerializer(OffsetDateTime.class, offsetDateTimeSerializer);
        javaTimeModule.addDeserializer(OffsetDateTime.class, InstantDeserializer.OFFSET_DATE_TIME);

        final LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(dateTimeFormatter);

        javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        mapper.registerModule(javaTimeModule);
        mapper.findAndRegisterModules();
        mapper.setDateFormat(RFC3339DateFormat.getDateInstance());

        return mapper;
    }

}
