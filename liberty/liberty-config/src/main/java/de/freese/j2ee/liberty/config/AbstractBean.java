// Created: 24.05.2018
package de.freese.j2ee.liberty.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public abstract class AbstractBean
{
    /**
     *
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Erzeugt eine neue Instanz von {@link AbstractBean}.
     */
    protected AbstractBean()
    {
        super();

        getLogger().info("{}: Constructor called", getClass().getSimpleName());
    }

    /**
     * @return {@link Logger}
     */
    public Logger getLogger()
    {
        return this.logger;
    }

    /**
     *
     */
    @PostConstruct
    public void postConstruct()
    {
        getLogger().info("postConstruct");
    }
}
