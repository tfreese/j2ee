// Created: 24.05.2018
package de.freese.j2ee.liberty.config;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public abstract class AbstractBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected AbstractBean() {
        super();

        getLogger().info("{}: Constructor called", getClass().getSimpleName());
    }

    public Logger getLogger() {
        return this.logger;
    }

    @PostConstruct
    public void postConstruct() {
        getLogger().info("postConstruct");
    }
}
