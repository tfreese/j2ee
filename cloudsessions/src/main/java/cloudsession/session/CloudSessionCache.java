package cloudsession.session;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public class CloudSessionCache implements CloudSession {
    public static final String TIMEOUT = "timeout";
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudSessionCache.class);

    private static boolean isTimeoutReached(final Long timeout) {
        if (timeout != null) {
            return System.currentTimeMillis() > timeout;
        }

        return false;
    }

    private final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();
    private final CloudSession delegate;
    private final long sessionLiveTime;

    public CloudSessionCache(final CloudSession delegate, final Duration sessionLiveTime) {
        super();

        this.delegate = delegate;
        this.sessionLiveTime = sessionLiveTime.toMillis();
    }

    @Override
    public String getSessionValue(final String sessionID, final String name) {
        if (sessionID == null) {
            return null;
        }

        // Search in Cache.
        final Map<String, String> entry = cache.get(sessionID);

        if (entry == null) {
            // Not found in the Cache.
            LOGGER.info("no entry [{},{}] found in memory cache!", sessionID, name);

            return checkValueInCloudAndUpdateLocal(sessionID, name, null);
        }
        else if (isTimeoutReached(Optional.ofNullable(entry.get(TIMEOUT)).map(Long::valueOf).orElse(null))) {
            LOGGER.info("entry [{}] has timeout -> check if present in cloud!", sessionID);

            return checkValueInCloudAndUpdateLocal(sessionID, name, entry);
        }
        else {
            LOGGER.info("entry [{},{}] found in memory cache!", sessionID, name);
        }

        // Value found in local cache and timeout isn't reached -> renew timeout.
        renewTimeout(sessionID, entry);

        return entry.get(name);
    }

    @Override
    public void remove(final String sessionID) {
        delegate.remove(sessionID);
        cache.remove(sessionID);
    }

    @Override
    public void setSessionValue(final String sessionID, final String name, final String value) {
        LOGGER.info("setting entry [{},{},{}]", sessionID, name, value);

        final Map<String, String> entry = cache.computeIfAbsent(sessionID, key -> new ConcurrentHashMap<>());

        // Update value in Cache.
        entry.put(name, value);

        // Put value in the cloud.
        delegate.setSessionValue(sessionID, name, value);

        renewTimeout(sessionID, entry);
    }

    private String checkValueInCloudAndUpdateLocal(final String sessionID, final String name, final Map<String, String> entry) {
        final String cloudSessionValue = delegate.getSessionValue(sessionID, name);

        if (cloudSessionValue != null) {
            LOGGER.info("found value [{}] in persistent cache!", cloudSessionValue);

            if (entry == null) {
                // Set local entry and update timeout in the cloud.
                setSessionValue(sessionID, name, cloudSessionValue);
            }
            else {
                // Renew all timeouts.
                renewTimeout(sessionID, entry);
            }
        }
        else {
            // Remove session information.
            remove(sessionID);
        }

        return cloudSessionValue;
    }

    private void renewTimeout(final String sessionID, final Map<String, String> entry) {
        // Calculate new timeout.
        final String timeout = Long.toString(System.currentTimeMillis() + sessionLiveTime);

        // Renew cloud session timeout.
        delegate.setSessionValue(sessionID, TIMEOUT, timeout);

        LOGGER.info("renewTimeout: setting entry [{},{},{}]", sessionID, TIMEOUT, timeout);

        // Renew cache timeout.
        entry.put(TIMEOUT, timeout);
    }
}
