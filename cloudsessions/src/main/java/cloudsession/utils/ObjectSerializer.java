package cloudsession.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public final class ObjectSerializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(ObjectSerializer.class);

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static <T> T fromJson(final InputStream inputStream, final Class<T> valueType) {
        try {
            return JSON_MAPPER.readValue(inputStream, valueType);
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T fromJson(final InputStream inputStream, final TypeReference<T> typeReference) {
        try {
            return JSON_MAPPER.readValue(inputStream, typeReference);
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void toJson(final OutputStream outputStream, final Object o) {
        if (o == null) {
            return;
        }

        try {
            JSON_MAPPER.writeValue(outputStream, o);
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private ObjectSerializer() {
        super();
    }
}
