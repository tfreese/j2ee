// Created: 28.05.2018
package de.freese.j2ee.liberty.config;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SessionBean ohne Interface.
 *
 * @author Thomas Freese
 */
@Stateless
@LocalBean
public class NoViewBean
{
    /**
     *
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Erstellt ein neues {@link NoViewBean} Object.
     */
    public NoViewBean()
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
     * @return {@link String}
     */
    public String getValue()
    {
        return "Hello World";
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
