package cloudsession.utils;

import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author Thomas Freese
 */
public final class JsonUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            // Don't serialize empty values.
            .changeDefaultPropertyInclusion(value -> value.withValueInclusion(JsonInclude.Include.NON_EMPTY))
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .build();

    public static <T> T fromJson(final InputStream inputStream, final Class<T> valueType) {
        try {
            return JSON_MAPPER.readValue(inputStream, valueType);
        }
        catch (final RuntimeException ex) {
            throw ex;
        }
        catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T fromJson(final InputStream inputStream, final TypeReference<T> typeReference) {
        try {
            return JSON_MAPPER.readValue(inputStream, typeReference);
        }
        catch (final RuntimeException ex) {
            throw ex;
        }
        catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void toJson(final OutputStream outputStream, final Object o) {
        if (o == null) {
            return;
        }

        JSON_MAPPER.writeValue(outputStream, o);
    }

    private JsonUtils() {
        super();
    }
}
