package cloudsession.utils;

import java.io.InputStream;
import java.io.OutputStream;

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
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    public static <T> T fromJson(final InputStream inputStream, final Class<T> valueType) {
        try {
            return JSON_MAPPER.readValue(inputStream, valueType);
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

        JSON_MAPPER.writeValue(outputStream, o);
    }

    private JsonUtils() {
        super();
    }
}
