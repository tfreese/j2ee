// Created: 20.05.2018
package de.freese.liberty;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Base Path under Context-Root for REST-Services.
 *
 * @author Thomas Freese
 */
@ApplicationPath("my-app")
public class SystemApplication extends Application {
}
