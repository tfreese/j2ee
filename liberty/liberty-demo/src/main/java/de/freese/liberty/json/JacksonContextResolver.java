// Created: 22 März 2025
package de.freese.liberty.json;

import java.time.Duration;
import java.util.Optional;
import java.util.TimeZone;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Provider
@RequestScoped
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {
    private static final Cache<String, ObjectMapper> CACHE = Caffeine.newBuilder().expireAfterWrite(Duration.ofHours(1L)).build();
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonContextResolver.class);

    private static ObjectMapper createObjectMapper() {
        LOGGER.info("create instance: {}", Thread.currentThread().getName());

        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new JavaTimeModule())
                .setTimeZone(TimeZone.getDefault())
                .disable(SerializationFeature.INDENT_OUTPUT)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        LOGGER.info("obtain instance for type: {}", Optional.ofNullable(type).map(Class::getSimpleName).orElse("null"));

        return CACHE.get(Thread.currentThread().getName(), key -> createObjectMapper());
    }

    @jakarta.enterprise.inject.Produces
    @ObjectMapperQualifier
    public ObjectMapper getObjectMapper() {
        return getContext(Object.class);
    }
}
