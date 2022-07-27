package cloudsession.session;

import java.util.Optional;

import javax.servlet.http.HttpSession;

/**
 * Interface für Zugriff auf Inhalt der {@link HttpSession}.
 *
 * @author Thomas Freese
 */
public interface CloudSession
{
    /**
     * @param sessionID String
     * @param name String
     *
     * @return Object
     */
    String getSessionValue(String sessionID, String name);

    default Long getSessionValueAsLong(String sessionID, String name)
    {
        String value = getSessionValue(sessionID, name);

        return Optional.ofNullable(value).map(Long::valueOf).orElse(null);
    }

    /**
     * @param sessionID String
     */
    void remove(String sessionID);

    /**
     * @param sessionID String
     * @param name String
     * @param value String
     */
    void setSessionValue(String sessionID, String name, String value);
}
