// Created: 14 März 2025
package de.freese.liberty.kryo;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author Thomas Freese
 */
@Provider
@Consumes(KryoReaderWriter.KRYO_MEDIA_TYPE)
@Produces(KryoReaderWriter.KRYO_MEDIA_TYPE)
public final class KryoReaderWriter<T> implements MessageBodyWriter<T>, MessageBodyReader<T> {
    public static final String KRYO_MEDIA_TYPE = "application/x-kryo";

    // @Inject
    // private Kryo kryo;
    private final KryoProvider kryoProvider = new KryoProvider();

    @Override
    public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return KRYO_MEDIA_TYPE.equalsIgnoreCase(mediaType.toString());
    }

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return KRYO_MEDIA_TYPE.equalsIgnoreCase(mediaType.toString());
    }

    @Override
    public T readFrom(final Class<T> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders,
                      final InputStream entityStream)
            throws WebApplicationException {
        final Input input = new Input(entityStream);

        return getKryo().readObjectOrNull(input, type);
    }

    @Override
    public void writeTo(final T t, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
            throws WebApplicationException {
        final Output output = new Output(entityStream);

        getKryo().writeObjectOrNull(output, t, type);
    }

    private Kryo getKryo() {
        // return kryo;
        return kryoProvider.getKryo();
    }
}
