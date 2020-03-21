package servlets;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cloudsession.CloudSession;
import cloudsession.CloudSessionCache;
import cloudsession.LocalSessionService;

/**
 * @author Thomas Freese
 */
public class NonStickySessionTest extends HttpServlet
{
    /**
     *
     */
    private static final String CREATION_TIME = "creationTime";

    /**
     *
     */
    private static final String JSESSIONID = "JSESSIONID=";

    /**
     *
     */
    private static final String LAST_ACCESS_TIME = "lastAccessTime";

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private static final String USER = "user";

    /**
     *
     */
    private final CloudSession cloudSession;

    /**
     *
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Erstellt ein neues {@link NonStickySessionTest} Object.
     */
    public NonStickySessionTest()
    {
        super();

        // CloudSession cs = new AmazonSessionService();
        CloudSession cs = new LocalSessionService();
        this.cloudSession = new CloudSessionCache(cs, CloudSessionCache.DEFAULT_LIVE_TIME);
    }

    /**
     * @param creationTime long
     * @return String
     */
    private String formatDate(final long creationTime)
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(creationTime);

        return gc.getTime().toString();
    }

    /**
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response)
    {
        StringBuilder html = new StringBuilder();
        html.append("<html>").append("\n");

        if (request.getParameter("invalidate") != null)
        {
            html.append("Session invalidated").append("\n");
            html.append("</html>");

            request.getSession().invalidate();

            try (ServletOutputStream outputStream = response.getOutputStream())
            {
                outputStream.print(html.toString());
            }
            catch (IOException ex)
            {
                this.logger.error(null, ex);
            }

            return;
        }

        // print request headers
        html.append("<font size=\"2\" face=\"courier\">").append("\n");
        // out +=
        // "Request Headers<br/>"+printHeaders(request)+"<br/><br/>"+printParameters(request)+
        // "<br/>session:"+request.getSession()+"<br/>";
        // out += "</font>";

        // check session ID from request
        String cookieSessionID = request.getHeader("cookie");

        HttpSession session = null;

        if (request.getSession() != null)
        {
            session = request.getSession();

            // TODO SessionSwitch ss = new SessionSwitch(session);
            Long lat = (Long) this.cloudSession.getSessionValue(cookieSessionID, LAST_ACCESS_TIME);

            if (lat != null)
            {
                html.append("lastAccessTime: [").append(new Date(lat)).append("]<br/>").append("\n");
                html.append("verbleibende Zeit im cache: [").append((((CloudSessionCache.DEFAULT_LIVE_TIME * 1000) + lat) - System.currentTimeMillis()))
                        .append("] millis<br/>").append("\n");
            }
            else
            {
                html.append("lastAccessTime not in cache!!!<br/>").append("\n");
            }

            this.cloudSession.setSessionValue(cookieSessionID, CREATION_TIME, session.getCreationTime());
            this.cloudSession.setSessionValue(cookieSessionID, LAST_ACCESS_TIME, System.currentTimeMillis());

            html.append("cookieSessionID:[").append(cookieSessionID).append("]<br/>\ncreationTime:[").append(formatDate(session.getCreationTime()))
                    .append("]<br/>").append("\n");
            html.append("current Time:[").append(formatDate(System.currentTimeMillis())).append("]<br/>").append("\n");

            html.append("<br/>request.getSession().getId() : [").append(session.getId()).append("]").append("\n");
            html.append("<br/>cookieSessionID              : [").append(cookieSessionID).append("]<br/>").append("\n");

            if (!session.getId().equals(cookieSessionID))
            {
                // check if cookie session has no timeout
                // TODO
                // if no timeout set new cookieSessionID and delete old cookieSessionID
                Long val = (Long) this.cloudSession.getSessionValue(cookieSessionID, CREATION_TIME);

                if (val != null)
                {
                    this.cloudSession.setSessionValue(JSESSIONID + session.getId(), CREATION_TIME, val);
                    // TODO cs.remove(cookieSessionID);
                }
            }

            String reqUser = request.getParameter(USER);

            if (reqUser != null)
            {
                // Train t = new Train();
                // t.setHour(4);
                // t.setItemName("wert");
                // t.setMinute(56);
                // t.setTrainName("trainName");
                // cs.setSessionValue(cookieSessionID, "train", t);
                //
                this.cloudSession.setSessionValue(cookieSessionID, USER, reqUser);
                html.append("set to session").append(cookieSessionID).append(" user:").append(reqUser).append("<br/>").append("\n");
            }
            else
            {
                String csUser = (String) this.cloudSession.getSessionValue(cookieSessionID, USER);
                html.append("get from session").append(cookieSessionID).append(":").append(csUser).append("<br/>").append("\n");
                // Train t = (Train)cs.getSessionValue(cookieSessionID, "train");
            }
        }

        html.append("<br/><a href=\"./Session?invalidate=true\">kill session</a>").append("\n");
        html.append("</html>");

        try (ServletOutputStream outputStream = response.getOutputStream())
        {
            outputStream.print(html.toString());
        }
        catch (IOException ex)
        {
            this.logger.error(null, ex);
        }
    }

    // private String printParameters(HttpServletRequest request) {
    // Enumeration<?> names = request.getParameterNames();
    // String res = "";
    // while (names.hasMoreElements()) {
    // String nextName = (String)names.nextElement();
    // res += nextName + ":" + request.getParameter(nextName)+"<br/>";
    // }
    // return res;
    // }
    //
    // private String printHeaders(HttpServletRequest request) {
    // Enumeration<?> names = request.getHeaderNames();
    // String res = "";
    // while (names.hasMoreElements()) {
    // String nextName = (String)names.nextElement();
    // res += nextName + ":[" + request.getHeader(nextName)+"]<br/>";
    // }
    // return res;
    // }
}
