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
        if (!(request instanceof HttpServletRequest httpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        final HttpSession httpSession = httpServletRequest.getSession(true);

        final boolean isLoggedIn = httpSession.getAttribute(LoginController.ATTRIBUTE_USER_NAME) != null;
        final String requestedUri = httpServletRequest.getRequestURI();

        if (isLoggedIn
                || requestedUri.contains("login.xhtml")
                || requestedUri.contains("jakarta.faces.resource")
                || requestedUri.endsWith(".css")
                || requestedUri.endsWith(".gif")
        ) {
            chain.doFilter(request, response);
        }
        else {
            // Programmatic Login.
            // httpSession.setAttribute(LoginController.ATTRIBUTE_USER_NAME, "myUserId");
            httpSession.setAttribute(LoginController.ATTRIBUTE_REQUESTED_URL, requestedUri);

            ((HttpServletResponse) response).sendRedirect(httpServletRequest.getContextPath() + "/login.xhtml");
        }
    }
}
