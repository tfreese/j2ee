package cloudsession.session;

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
    Object getSessionValue(String sessionID, String name);

    /**
     * @param sessionID String
     */
    void remove(String sessionID);

    /**
     * @param sessionID String
     * @param name String
     * @param value Object
     */
    void setSessionValue(String sessionID, String name, Object value);
}
