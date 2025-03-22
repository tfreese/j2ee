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
    public static final String USER_ATTRIBUTE_NAME = "userName";

    @Serial
    private static final long serialVersionUID = 2284719796428287116L;

    @Inject
    private LoginService loginService;

    private String message;
    private String password;
    private String userName;

    public String getMessage() {
        return message;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    @PostConstruct
    public void init() {
        // requestedUrl = (String) request.getSession().getAttribute(REQUESTED_URL_BEFORE_LOGIN);
    }

    public String login() {
        final boolean result = loginService.login(userName, password);

        if (result) {
            // get Http Session and store username
            final HttpSession session = Util.getSession();
            session.setAttribute(USER_ATTRIBUTE_NAME, userName);

            // FacesContext.getCurrentInstance().getExternalContext().redirect(requestedUrl == null ? contextRoot : requestedUrl);

            return "index";
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please Try Again!"));

            // invalidate session, and redirect to other pages
            return "login";
        }
    }

    public String logout() {
        final HttpSession session = Util.getSession();
        session.invalidate();

        return "login";
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }
}
