// Created: 22 März 2025
package de.freese.liberty.json;

import java.time.Duration;
import java.util.Optional;
import java.util.TimeZone;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author Thomas Freese
 */
@Provider // Must bei part of the WAR, and not in a Dependency.
@RequestScoped
public class JacksonContextResolver implements ContextResolver<JsonMapper> {
    private static final Cache<String, JsonMapper> CACHE = Caffeine.newBuilder().expireAfterWrite(Duration.ofHours(1L)).build();
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonContextResolver.class);

    private static JsonMapper createJsonMapper() {
        LOGGER.info("create instance: {}", Thread.currentThread().getName());

        return JsonMapper.builder()
                .changeDefaultPropertyInclusion(value -> value.withValueInclusion(JsonInclude.Include.NON_NULL))
                .defaultTimeZone(TimeZone.getDefault())
                .disable(SerializationFeature.INDENT_OUTPUT)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
    }

    @Override
    public JsonMapper getContext(final Class<?> type) {
        LOGGER.info("obtain instance for type: {}", Optional.ofNullable(type).map(Class::getSimpleName).orElse("null"));

        return CACHE.get(Thread.currentThread().getName(), key -> createJsonMapper());
    }

    @jakarta.enterprise.inject.Produces
    @JsonMapperQualifier
    public JsonMapper getJsonMapper() {
        return getContext(Object.class);
    }
}
