// Created: 20.05.2018
package de.freese.liberty.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Basis Pfad aller REST-Services.
 *
 * @author Thomas Freese
 */
@ApplicationPath("my-liberty")
public class SystemApplication extends Application {
}
