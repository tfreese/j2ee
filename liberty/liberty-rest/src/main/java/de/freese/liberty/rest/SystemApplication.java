// Created: 20.05.2018
package de.freese.liberty.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Basis Pfad aller REST-Services.
 *
 * @author Thomas Freese
 */
@ApplicationPath("liberty")
public class SystemApplication extends Application
{
    /**
     * Erstellt ein neues {@link SystemApplication} Object.
     */
    public SystemApplication()
    {
        super();
    }
}
