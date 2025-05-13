// Created: 20.05.2018
package de.freese.liberty;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Base Path under Context-Root for REST-Services.
 *
 * @author Thomas Freese
 */
@ApplicationPath("my-app")
@WebListener
public class SystemApplication extends Application implements ServletContextListener {
    private static String contextRoot = "/appl";

    public static String getContextRoot() {
        return contextRoot;
    }

    private static void setContextRoot(final String contextRoot) {
        SystemApplication.contextRoot = contextRoot;
    }

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        synchronized (this) {
            setContextRoot(event.getServletContext().getContextPath());
        }
    }
}
