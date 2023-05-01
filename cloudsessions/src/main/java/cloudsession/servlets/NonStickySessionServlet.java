package cloudsession.servlets;

import java.io.IOException;
import java.io.Serial;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import cloudsession.session.CloudSession;
import cloudsession.session.CloudSessionCache;
import cloudsession.session.CloudSessionLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <a href="http://localhost:8088/session">session-demo</a>
 *
 * @author Thomas Freese
 */
@WebServlet(name = "NonStickySessionServlet", urlPatterns = {"/session"}, loadOnStartup = 1)
public class NonStickySessionServlet extends HttpServlet {
    private static final String CREATION_TIME = "creationTime";

    private static final String LAST_ACCESS_TIME = "lastAccessTime";

    private static final Duration SESSION_LIVE_TIME = Duration.ofSeconds(15);

    private static final String USER = "user";

    @Serial
    private static final long serialVersionUID = 1L;

    static String formatDate(final long creationTime) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(creationTime);

        return gc.getTime().toString();
    }

    static StringBuilder printHeaders(HttpServletRequest request) {
        Enumeration<?> names = request.getHeaderNames();
        StringBuilder html = new StringBuilder();
        html.append("<table>\n");
        html.append("<tr><th colspan=\"2\" style=\"text-align: center\">Request Headers</th></tr>\n");

        while (names.hasMoreElements()) {
            String nextName = (String) names.nextElement();
            html.append("<tr><td>").append(nextName).append("</td><td>").append(request.getHeader(nextName)).append("</td></tr>\n");
        }

        html.append("</table>\n");

        return html;
    }

    static StringBuilder printParameters(HttpServletRequest request) {
        Enumeration<?> names = request.getParameterNames();
        StringBuilder html = new StringBuilder();
        html.append("<table>\n");
        html.append("<tr><th colspan=\"2\" style=\"text-align: center\">Request Parameters</th></tr>\n");

        while (names.hasMoreElements()) {
            String nextName = (String) names.nextElement();
            html.append("<tr><td>").append(nextName).append("</td><td>").append(request.getParameter(nextName)).append("</td></tr>\n");
        }

        html.append("</table>\n");

        return html;
    }

    private final transient CloudSession cloudSession;
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    public NonStickySessionServlet() {
        super();

        // CloudSession cs = new CloudSessionAmazon();
        CloudSession cs = new CloudSessionLocal();
        this.cloudSession = new CloudSessionCache(cs, SESSION_LIVE_TIME);
    }

    /**
     * @see jakarta.servlet.http.HttpServlet#service(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
     */
    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) {
        StringBuilder html = new StringBuilder();
        html.append("<html>\n");

        html.append(printHeaders(request)).append("<br/><br/>\n");
        html.append(printParameters(request)).append("<br/><br/>\n");

        if (request.getParameter("invalidate") != null) {
            request.getSession().invalidate();

            html.append("Session invalidated\n");
            html.append("</html>");

            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.print(html.toString());
                outputStream.flush();
            }
            catch (IOException ex) {
                this.logger.error(ex.getMessage(), ex);
            }

            return;
        }

        html.append("<table>\n");

        if (request.getSession() != null) {
            HttpSession session = request.getSession();

            //String sessionID = session.getId(); // This is the current Session, but we want the old one.
            String sessionID = null;

            String cookie = request.getHeader("cookie");

            if (cookie != null) {
                sessionID = cookie.split("=")[1];
            }
            else {
                cookie = "";
            }

            html.append("<tr><td>cookieSessionID</td><td>").append(cookie).append("</td></tr>\n");
            html.append("<tr><td>sessionID</td><td>").append(session.getId()).append("</td></tr>\n");

            // TODO SessionSwitch ss = new SessionSwitch(session);
            Long lat = this.cloudSession.getSessionValueAsLong(sessionID, LAST_ACCESS_TIME);

            if (lat != null) {
                html.append("<tr><td>lastAccessTime</td><td>").append(LocalDateTime.ofInstant(Instant.ofEpochMilli(lat), ZoneId.systemDefault())).append("</td></tr>\n");
                html.append("<tr><td>verbleibende Zeit im Cache</td><td>").append((SESSION_LIVE_TIME.toMillis() + lat) - System.currentTimeMillis()).append(" millis</td></tr>\n");
            }
            else {
                html.append("<tr><td>lastAccessTime</td><td>Not in Cache !!!</td></tr>\n");
            }

            this.cloudSession.setSessionValue(sessionID, CREATION_TIME, Long.toString(session.getCreationTime()));
            this.cloudSession.setSessionValue(sessionID, LAST_ACCESS_TIME, Long.toString(System.currentTimeMillis()));

            html.append("<tr><td>creationTime</td><td>").append(LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getCreationTime()), ZoneId.systemDefault())).append("</td></tr>\n");
            html.append("<tr><td>current Time</td><td>").append(LocalDateTime.now()).append("</td></tr>\n");

            if (!session.getId().equals(sessionID)) {
                // check if cookie session has no timeout
                // TODO
                // if no timeout set new cookieSessionID and delete old cookieSessionID
                Long val = this.cloudSession.getSessionValueAsLong(sessionID, CREATION_TIME);

                if (val != null) {
                    this.cloudSession.setSessionValue(sessionID, CREATION_TIME, val.toString());
                    // TODO cs.remove(cookieSessionID);
                }
            }

            String reqUser = request.getParameter(USER);

            if (reqUser != null) {
                this.cloudSession.setSessionValue(sessionID, USER, reqUser);
                html.append("<tr><td>user</td><td>").append(reqUser).append("</td></tr>\n");
            }
            else {
                String csUser = this.cloudSession.getSessionValue(sessionID, USER);
                html.append("<tr><td>user</td><td>").append(csUser).append("</td></tr>\n");
            }
        }

        html.append("</table>\n");

        html.append("<br/><br/><a href=\"./session?invalidate=true\">kill session</a>").append("\n");
        html.append("</html>");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.print(html.toString());
            outputStream.flush();
        }
        catch (IOException ex) {
            this.logger.error(ex.getMessage(), ex);
        }
    }
}
