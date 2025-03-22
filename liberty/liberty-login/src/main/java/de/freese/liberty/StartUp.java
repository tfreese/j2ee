// Created: 14.12.2012
package de.freese.liberty;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
// @LocalBean
public class StartUp {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);

    @PostConstruct
    public void myPostConstruct() {
        LOGGER.info("myPostConstruct");
    }

    @PreDestroy
    public void myPreDestroy() {
        LOGGER.info("myPreDestroy");
    }
}
