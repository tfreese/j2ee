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
    private static final Logger LOGGER = LoggerFactory.getLogger(NonStickySessionServlet.class);
    private static final Duration SESSION_LIVE_TIME = Duration.ofSeconds(15);
    private static final String USER = "user";

    @Serial
    private static final long serialVersionUID = 1L;

    static String formatDate(final long creationTime) {
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(creationTime);

        return gc.getTime().toString();
    }

    static StringBuilder printHeaders(final HttpServletRequest request) {
        final Enumeration<?> names = request.getHeaderNames();
        final StringBuilder html = new StringBuilder();
        html.append("<table>").append(System.lineSeparator());
        html.append("<tr><th colspan=\"2\" style=\"text-align: center\">Request Headers</th></tr>").append(System.lineSeparator());

        while (names.hasMoreElements()) {
            final String nextName = (String) names.nextElement();
            html.append("<tr><td>").append(nextName).append("</td><td>").append(request.getHeader(nextName)).append("</td></tr>").append(System.lineSeparator());
        }

        html.append("</table>").append(System.lineSeparator());

        return html;
    }

    static StringBuilder printParameters(final HttpServletRequest request) {
        final Enumeration<?> names = request.getParameterNames();
        final StringBuilder html = new StringBuilder();
        html.append("<table>").append(System.lineSeparator());
        html.append("<tr><th colspan=\"2\" style=\"text-align: center\">Request Parameters</th></tr>").append(System.lineSeparator());

        while (names.hasMoreElements()) {
            final String nextName = (String) names.nextElement();
            html.append("<tr><td>").append(nextName).append("</td><td>").append(request.getParameter(nextName)).append("</td></tr>").append(System.lineSeparator());
        }

        html.append("</table>").append(System.lineSeparator());

        return html;
    }

    private final transient CloudSession cloudSession;

    public NonStickySessionServlet() {
        super();

        // final CloudSession cs = new CloudSessionAmazon();
        final CloudSession cs = new CloudSessionLocal();
        cloudSession = new CloudSessionCache(cs, SESSION_LIVE_TIME);
    }

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) {
        final StringBuilder html = new StringBuilder();
        html.append("<html>").append(System.lineSeparator());

        html.append(printHeaders(request)).append("<br/><br/>").append(System.lineSeparator());
        html.append(printParameters(request)).append("<br/><br/>").append(System.lineSeparator());

        if (request.getParameter("invalidate") != null) {
            request.getSession().invalidate();

            html.append("Session invalidated").append(System.lineSeparator());
            html.append("</html>");

            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.print(html.toString());
                outputStream.flush();
            }
            catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }

            return;
        }

        html.append("<table>").append(System.lineSeparator());

        if (request.getSession() != null) {
            final HttpSession session = request.getSession();

            // String sessionID = session.getId(); // This is the current Session, but we want the old one.
            String sessionID = null;

            String cookie = request.getHeader("cookie");

            if (cookie != null) {
                sessionID = cookie.split("=")[1];
            }
            else {
                cookie = "";
            }

            html.append("<tr><td>cookieSessionID</td><td>").append(cookie).append("</td></tr>").append(System.lineSeparator());
            html.append("<tr><td>sessionID</td><td>").append(session.getId()).append("</td></tr>").append(System.lineSeparator());

            // SessionSwitch ss = new SessionSwitch(session);
            final Long lat = cloudSession.getSessionValueAsLong(sessionID, LAST_ACCESS_TIME);

            if (lat != null) {
                html.append("<tr><td>lastAccessTime</td><td>").append(LocalDateTime.ofInstant(Instant.ofEpochMilli(lat), ZoneId.systemDefault())).append("</td></tr>")
                        .append(System.lineSeparator());
                html.append("<tr><td>verbleibende Zeit im Cache</td><td>").append((SESSION_LIVE_TIME.toMillis() + lat) - System.currentTimeMillis()).append(" millis</td></tr>")
                        .append(System.lineSeparator());
            }
            else {
                html.append("<tr><td>lastAccessTime</td><td>Not in Cache !!!</td></tr>").append(System.lineSeparator());
            }

            cloudSession.setSessionValue(sessionID, CREATION_TIME, Long.toString(session.getCreationTime()));
            cloudSession.setSessionValue(sessionID, LAST_ACCESS_TIME, Long.toString(System.currentTimeMillis()));

            html.append("<tr><td>creationTime</td><td>").append(LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getCreationTime()), ZoneId.systemDefault()))
                    .append("</td></tr>").append(System.lineSeparator());
            html.append("<tr><td>current Time</td><td>").append(LocalDateTime.now()).append("</td></tr>").append(System.lineSeparator());

            if (!session.getId().equals(sessionID)) {
                // Check if cookie session has no timeout.
                // If no timeout set new sessionID and delete old sessionID.
                final Long val = cloudSession.getSessionValueAsLong(sessionID, CREATION_TIME);

                if (val != null) {
                    cloudSession.setSessionValue(sessionID, CREATION_TIME, val.toString());
                    // TODO cloudSession.remove(sessionID);
                }
            }

            final String reqUser = request.getParameter(USER);

            if (reqUser != null) {
                cloudSession.setSessionValue(sessionID, USER, reqUser);
                html.append("<tr><td>user</td><td>").append(reqUser).append("</td></tr>").append(System.lineSeparator());
            }
            else {
                final String csUser = cloudSession.getSessionValue(sessionID, USER);
                html.append("<tr><td>user</td><td>").append(csUser).append("</td></tr>").append(System.lineSeparator());
            }
        }

        html.append("</table>").append(System.lineSeparator());

        html.append("<br/><br/><a href=\"./session?invalidate=true\">kill session</a>").append(System.lineSeparator());
        html.append("</html>");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.print(html.toString());
            outputStream.flush();
        }
        catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
