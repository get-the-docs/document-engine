package net.videki.templateutils.api.document.api.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class JacksonConfiguration {

    @Primary
    @Bean
    public ObjectMapper jsonMapper() {
        final ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        jsonMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        jsonMapper.configOverride(List.class).setSetterInfo(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY));
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        final OffsetDateTimeSerializer offsetDateTimeSerializer = OffsetDateTimeSerializer.INSTANCE;
        javaTimeModule.addSerializer(OffsetDateTime.class, offsetDateTimeSerializer);
        javaTimeModule.addDeserializer(OffsetDateTime.class, InstantDeserializer.OFFSET_DATE_TIME);
        
        final LocalDateTimeDeserializer localDateTimeDeserializer =
            new LocalDateTimeDeserializer(dateTimeFormatter);
    
        javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        jsonMapper.registerModule(javaTimeModule);
        jsonMapper.findAndRegisterModules();
        jsonMapper.setDateFormat(RFC3339DateFormat.getDateInstance());

        return jsonMapper;
    }

}