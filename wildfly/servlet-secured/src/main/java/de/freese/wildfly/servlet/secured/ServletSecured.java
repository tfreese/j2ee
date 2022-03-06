// Created on 13.12.2015 11:55:38
package de.freese.wildfly.servlet.secured;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("serial")
@WebServlet("/SecuredServlet")
@ServletSecurity(@HttpConstraint(rolesAllowed =
        {
                "quickstarts"
        }))
public class ServletSecured extends HttpServlet
{
    /**
     *
     */
    private static final String PAGE_FOOTER = "</body></html>";
    /**
     *
     */
    private static final String PAGE_HEADER = "<html><head><title>servlet-security</title></head><body>";

    /**
     * Erstellt ein neues Object.
     */
    public ServletSecured()
    {
        super();
    }

    /**
     * @see jakarta.servlet.http.HttpServlet#doGet(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
    {
        try (PrintWriter writer = resp.getWriter())
        {
            // Get security principal
            Principal principal = req.getUserPrincipal();
            String principalName = principal == null ? "" : principal.getName();
            // Get user name from login principal
            String remoteUser = req.getRemoteUser();
            // Get authentication type
            String authType = req.getAuthType();

            writer.println(PAGE_HEADER);
            writer.println("<h1>" + "Successfully called Secured Servlet " + "</h1>");
            writer.println("<p>" + "Principal  : " + principalName + "</p>");
            writer.println("<p>" + "Remote User : " + remoteUser + "</p>");
            writer.println("<p>" + "Authentication Type : " + authType + "</p>");
            writer.println(PAGE_FOOTER);
        }
    }
}
