// Created:20.05.2018
package de.freese.j2ee.liberty.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Basis Pfad aller REST-Services.
 *
 * @author Thomas Freese
 */
@ApplicationPath("rest")
public class RestApplication extends Application {

}
