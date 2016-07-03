// Erzeugt: 10.06.2015
package de.freese.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * @author Thomas Freese
 */
@Configuration
public class Initializer implements ServletContextInitializer
{
    /**
     * Erstellt ein neues {@link Initializer} Object.
     */
    public Initializer()
    {
        super();
    }

    /**
     * @see org.springframework.boot.context.embedded.ServletContextInitializer#onStartup(javax.servlet.ServletContext)
     */
    @Override
    public void onStartup(final ServletContext sc) throws ServletException
    {
        // sc.setInitParameter("com.sun.faces.forceLoadConfiguration", "TRUE");
        sc.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        sc.setInitParameter("javax.faces.STATE_SAVING_METHOD", "server");
        sc.setInitParameter("com.sun.faces.compressViewState", "true");
        sc.setInitParameter("org.primefaces.extensions.DELIVER_UNCOMPRESSED_RESOURCES", "false");
        sc.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
        sc.setInitParameter("javax.faces.VALIDATE_EMPTY_FIELDS", "true");
        sc.setInitParameter("com.sun.faces.enableMissingResourceLibraryDetection", "true");
        sc.setInitParameter("javax.faces.CONFIG_FILES", "/WEB-INF/faces-config.xml");
        sc.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
        sc.setInitParameter("primefaces.THEME", "aristo");
    }
}
