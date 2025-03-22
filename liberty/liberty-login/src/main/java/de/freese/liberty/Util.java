// Created: 22 März 2025
package de.freese.liberty;

import java.util.Optional;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import de.freese.liberty.login.LoginController;

/**
 * @author Thomas Freese
 */
public final class Util {
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.
                getCurrentInstance().
                getExternalContext()
                .getRequest();
    }

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.
                getCurrentInstance().
                getExternalContext().
                getSession(false);
    }

    public static String getUserName() {
        return Optional.ofNullable(getSession().getAttribute(LoginController.USER_ATTRIBUTE_NAME)).map(Object::toString).orElse(null);
    }

    private Util() {
        super();
    }
}
