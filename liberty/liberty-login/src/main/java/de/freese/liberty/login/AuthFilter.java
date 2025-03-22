// Created: 22 März 2025
package de.freese.liberty.login;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author Thomas Freese
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public final class AuthFilter implements Filter {
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest req)) {
            chain.doFilter(request, response);
            return;
        }

        final HttpSession session = req.getSession(false);

        final boolean isLoggedIn = session != null && session.getAttribute(LoginController.USER_ATTRIBUTE_NAME) != null;
        final String reqURI = req.getRequestURI();

        if (isLoggedIn || reqURI.contains("login.xhtml") || reqURI.contains("jakarta.faces.resource")) {
            chain.doFilter(request, response);
        }
        else {
            // Programmatic Login
            // session.setAttribute(LoginController.USER_ATTRIBUTE_NAME, "myUserId");

            ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/login.xhtml");
        }
    }
}
