// Created: 22 März 2025
package de.freese.liberty.login;

import java.io.Serial;
import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import de.freese.liberty.Util;

/**
 * <a href="http://localhost:7080/liberty-login">localhost</a>
 *
 * @author Thomas Freese
 */
@Named
@SessionScoped
public class LoginController implements Serializable {
    public static final String ATTRIBUTE_REQUESTED_URL = "REQUESTED_URL";
    public static final String ATTRIBUTE_USER_NAME = "USER_NAME";

    @Serial
    private static final long serialVersionUID = 2284719796428287116L;

    @Inject
    private LoginService loginService;

    private String password;
    private String requestedUri;
    private String userName;

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    @PostConstruct
    public void init() {
        requestedUri = (String) Util.getSession().getAttribute(ATTRIBUTE_REQUESTED_URL);
    }

    public void login() {
        try {
            final boolean loggedIn = loginService.login(userName, password);

            final HttpServletRequest httpServletRequest = Util.getRequest();
            final HttpSession httpSession = Util.getSession();

            if (loggedIn) {
                httpSession.setAttribute(ATTRIBUTE_USER_NAME, userName);

                // return "index";
                FacesContext.getCurrentInstance().getExternalContext().redirect(requestedUri == null || !requestedUri.contains("login.xhtml")
                        ? httpServletRequest.getContextPath() + "/index.xhtml"
                        : requestedUri);
            }
            else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Login", "Please try Again."));

                // Invalidate session.
                httpSession.invalidate();

                // return "login";

                // FacesMessage will not shown.
                // FacesContext.getCurrentInstance().getExternalContext().redirect(requestedUri == null ? httpServletRequest.getContextPath() + "/login.xhtml" : requestedUri);
            }
        }
        catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Login", ex.getMessage()));
        }
    }

    public void logout() {
        try {
            final HttpSession httpSession = Util.getSession();
            httpSession.invalidate();

            final HttpServletRequest httpServletRequest = Util.getRequest();

            // return "login";
            FacesContext.getCurrentInstance().getExternalContext().redirect(httpServletRequest.getContextPath() + "/login.xhtml");
        }
        catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Logout", ex.getMessage()));
        }
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }
}
