// Created: 18 Apr. 2025
package de.freese.liberty.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Thomas Freese
 */
@Provider
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class JsonReaderWriter implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
    // @Inject
    // private JacksonProvider jacksonProvider;

    @Inject
    @ObjectMapperQualifier
    private ObjectMapper objectMapper;

    // @Context
    // private Providers providers;

    @Override
    public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return MediaType.APPLICATION_JSON.equalsIgnoreCase(mediaType.toString());
    }

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return MediaType.APPLICATION_JSON.equalsIgnoreCase(mediaType.toString());
    }

    @Override
    public Object readFrom(final Class<Object> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType,
                           final MultivaluedMap<String, String> httpHeaders,
                           final InputStream entityStream) throws IOException, WebApplicationException {
        return getObjectMapper().readValue(entityStream, type);
    }

    @Override
    public void writeTo(final Object t, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream entityStream) throws IOException, WebApplicationException {
        getObjectMapper().writeValue(entityStream, t);
    }

    // @PostConstruct
    // void init() {
    //     final ContextResolver<ObjectMapper> resolver = providers.getContextResolver(ObjectMapper.class, MediaType.APPLICATION_JSON_TYPE);
    //     final ObjectMapper om = resolver.getContext(ObjectMapper.class);
    // }

    private ObjectMapper getObjectMapper() {
        return objectMapper;
        // return jacksonProvider.getObjectMapper();
    }
}
