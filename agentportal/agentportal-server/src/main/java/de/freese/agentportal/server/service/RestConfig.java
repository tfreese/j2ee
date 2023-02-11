package de.freese.agentportal.server.service;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * @author Thomas Freese
 */
@ApplicationPath("/rest")
public class RestConfig extends Application {
    public RestConfig() {
        super();
    }
}
