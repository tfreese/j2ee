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

    private static boolean isTimeoutReached(final Long timeout) {
        if (timeout != null) {
            return System.currentTimeMillis() > timeout;
        }

        return false;
    }

    private final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    private final CloudSession cloudSession;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final long sessionLiveTime;

    public CloudSessionCache(final CloudSession cloudSession, final Duration sessionLiveTime) {
        super();

        this.cloudSession = cloudSession;
        this.sessionLiveTime = sessionLiveTime.toMillis();
    }

    @Override
    public String getSessionValue(final String sessionID, final String name) {
        // search in my cache
        Map<String, String> entry = this.cache.get(sessionID);

        if (entry == null) {
            // if not found in cache ...
            this.logger.info("no entry [{},{}] found in memory cache!", sessionID, name);

            return checkValueInCloudAndUpdateLocal(sessionID, name, null);
        }
        else if (isTimeoutReached(Optional.ofNullable(entry.get(TIMEOUT)).map(Long::valueOf).orElse(null))) {
            this.logger.info("entry [{}] has timeout -> check if present in cloud!", sessionID);

            return checkValueInCloudAndUpdateLocal(sessionID, name, entry);
        }
        else {
            this.logger.info("entry [{},{}] found in memory cache!", sessionID, name);
        }

        // value found in local cache and timeout not reached -> renew timeout
        renewTimeout(sessionID, entry);

        return entry.get(name);
    }

    @Override
    public void remove(final String sessionID) {
        this.cloudSession.remove(sessionID);
        this.cache.remove(sessionID);
    }

    @Override
    public void setSessionValue(final String sessionID, final String name, final String value) {
        this.logger.info("setting entry [{},{},{}]", sessionID, name, value);

        Map<String, String> entry = this.cache.computeIfAbsent(sessionID, key -> new ConcurrentHashMap<>());

        // update value in my cache
        entry.put(name, value);

        // set value in cloud
        this.cloudSession.setSessionValue(sessionID, name, value);

        renewTimeout(sessionID, entry);
    }

    private String checkValueInCloudAndUpdateLocal(final String sessionID, final String name, final Map<String, String> entry) {
        String cloudSessionValue = this.cloudSession.getSessionValue(sessionID, name);

        if (cloudSessionValue != null) {
            this.logger.info("found value [{}] in persistent cache!", cloudSessionValue);

            if (entry == null) {
                // set local entry and update timeout in cloud
                setSessionValue(sessionID, name, cloudSessionValue);
            }
            else {
                // renew all timeouts
                renewTimeout(sessionID, entry);
            }
        }
        else {
            // remove session information
            remove(sessionID);
        }

        return cloudSessionValue;
    }

    private void renewTimeout(final String sessionID, final Map<String, String> entry) {
        // calculate new timeout
        String timeout = Long.toString(System.currentTimeMillis() + this.sessionLiveTime);

        // renew cloud session timeout
        this.cloudSession.setSessionValue(sessionID, TIMEOUT, timeout);

        this.logger.info("setting entry [{},{},{}]", sessionID, TIMEOUT, timeout);

        // renew this cache timeout
        entry.put(TIMEOUT, timeout);
    }
}
