// Created: 28.05.2018
package de.freese.j2ee.liberty.config;

import jakarta.annotation.PostConstruct;

/**
 * SessionBean without Interface.
 *
 * @author Thomas Freese
 */
//@Stateless
//@LocalBean
public class NoViewBean extends AbstractBean {
    public String getValue() {
        return "Hello World";
    }

    @Override
    @PostConstruct
    public void postConstruct() {
        getLogger().info("postConstruct");
    }
}
