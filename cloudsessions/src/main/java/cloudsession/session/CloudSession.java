package cloudsession.session;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

/**
 * Interface für Zugriff auf Inhalt der {@link HttpSession}.
 *
 * @author Thomas Freese
 */
public interface CloudSession
{
    String getSessionValue(String sessionID, String name);

    default Long getSessionValueAsLong(String sessionID, String name)
    {
        String value = getSessionValue(sessionID, name);

        return Optional.ofNullable(value).map(Long::valueOf).orElse(null);
    }

    void remove(String sessionID);

    void setSessionValue(String sessionID, String name, String value);
}
