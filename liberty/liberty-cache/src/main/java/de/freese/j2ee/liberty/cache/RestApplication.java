// Created:20.05.2018
package de.freese.j2ee.liberty.cache;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/****
 * Basis Pfad aller REST-Services.****
 *
 * @author Thomas Freese
 */
@ApplicationPath("/")
public class RestApplication extends Application
{
    /**
     * Erstellt ein neues {@link RestApplication} Object.
     */
    public RestApplication()
    {
        super();
    }
}
