package cloudsession.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import cloudsession.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Thomas Freese
 */
public class CloudSessionLocal implements CloudSession {
    private static final Path DATA_PATH = Paths.get(System.getProperty("java.io.tmpdir"), "cloudSession.json");

    private static void storeProps(final Map<String, Map<String, String>> map) {
        try (OutputStream outputStream = Files.newOutputStream(DATA_PATH, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            JsonUtils.toJson(outputStream, map);
            outputStream.flush();
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private Map<String, Map<String, String>> map;

    @Override
    public String getSessionValue(final String sessionID, final String name) {
        return getMap().computeIfAbsent(sessionID, key -> new HashMap<>()).get(name);
    }

    @Override
    public void remove(final String sessionID) {
        getMap().remove(sessionID);

        storeProps(getMap());
    }

    @Override
    public void setSessionValue(final String sessionID, final String name, final String value) {
        getMap().computeIfAbsent(sessionID, key -> new HashMap<>()).put(name, value);

        storeProps(getMap());
    }

    private Map<String, Map<String, String>> getMap() {
        if (map == null) {
            try {
                map = new ConcurrentHashMap<>();

                if (!Files.exists(DATA_PATH)) {
                    Files.createDirectories(DATA_PATH.getParent());
                    Files.createFile(DATA_PATH);

                    return map;
                }

                try (InputStream inputStream = Files.newInputStream(DATA_PATH, StandardOpenOption.READ)) {
                    final TypeReference<Map<String, Map<String, String>>> typeRef = new TypeReference<>() {
                    };

                    final Map<String, Map<String, String>> mapJson = Optional.ofNullable(JsonUtils.fromJson(inputStream, typeRef)).orElse(Map.of());

                    mapJson.forEach((key, value) -> map.put(key, new HashMap<>(value)));
                }

                // Remove old Session-Entries.
                map.values().removeIf(data ->
                        data.get(CloudSessionCache.TIMEOUT) != null && System.currentTimeMillis() > Long.parseLong(data.get(CloudSessionCache.TIMEOUT))
                );
            }
            catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        return map;
    }
}
