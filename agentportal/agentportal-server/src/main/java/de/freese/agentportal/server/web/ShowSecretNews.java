package de.freese.agentportal.server.web;

import java.io.IOException;
import java.io.Serial;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Thomas Freese
 */
@WebServlet("/showsecretnews")
// @ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.NONE, rolesAllowed =
// {
// "AgentPortalRoleHigh", "AgentPortalRoleLow"
// }))
public class ShowSecretNews extends AbstractServlet
{
    @Serial
    private static final long serialVersionUID = 2003269226079797807L;

    /**
     * @see jakarta.servlet.http.HttpServlet#service(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
     */
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
    {
        logCallerInfo(request);

        // if (request.isUserInRole("AgentPortalRoleHigh"))
        // {
        // request.getRequestDispatcher("/jsf/showSecretNews4High.xhtml").forward(request, response);
        // }
        // else if (request.isUserInRole("AgentPortalRoleLow"))
        // {
        request.getRequestDispatcher("/jsf/showSecretNews4Low.xhtml").forward(request, response);
        // }
        // else
        // {
        // throw new SecurityException("Invalid role!");
        // }
    }
}
