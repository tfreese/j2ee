// Created: 28.05.2018
package de.freese.j2ee.liberty.config;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SessionBean ohne Interface.
 *
 * @author Thomas Freese
 */
@Stateless
public class NoViewBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public NoViewBean() {
        super();

        getLogger().info("{}: Constructor called", getClass().getSimpleName());
    }

    public Logger getLogger() {
        return this.logger;
    }

    public String getValue() {
        return "Hello World";
    }

    @PostConstruct
    public void postConstruct() {
        getLogger().info("postConstruct");
    }
}
