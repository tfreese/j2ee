package de.freese.agentportal.server.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public abstract class AbstractServlet extends HttpServlet
{
    /**
     *
     */
    private static final long serialVersionUID = 8687076224457775615L;
    /**
     *
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Erstellt ein neues {@link AbstractServlet} Object.
     */
    protected AbstractServlet()
    {
        super();
    }

    /**
     * @return {@link Logger}
     */
    protected Logger getLogger()
    {
        return this.logger;
    }

    /**
     * @param request {@link HttpServletRequest}
     */
    protected void logCallerInfo(final HttpServletRequest request)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("user=");

        try
        {
            sb.append(request.getUserPrincipal().getName());
            sb.append(", role=");

            if (request.isUserInRole("AgentPortalRoleHigh"))
            {
                sb.append("AgentPortalRoleHigh");
            }
            else if (request.isUserInRole("AgentPortalRoleLow"))
            {
                sb.append("AgentPortalRoleLow");
            }
            else
            {
                sb.append("<unknown>");
            }
        }
        catch (Exception ex)
        {
            sb.append("<unknown>");
        }

        getLogger().info("Servlet 'ShowSecretNews' called from {}", sb);
    }
}
