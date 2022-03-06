// Created: 21.05.2013
package de.freese.agentportal.server.cdi;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public class Resources
{
    /**
     *
     */
    public static final String EM_UNIT = "agentPortalJPA";
    /**
     *
     */
    @Produces
    @AgentPortalDS
    @Resource(mappedName = "jdbc/agentPortal")
    private DataSource dataSource;
    /**
     *
     */
    @Produces
    @AgentPortalEM
    @PersistenceContext(unitName = EM_UNIT)
    private EntityManager entityManager;

    /**
     * Erstellt ein neues {@link Resources} Object.
     */
    public Resources()
    {
        super();
    }

    /**
     * @return {@link FacesContext}
     */
    @Produces
    public FacesContext getFacesContext()
    {
        return FacesContext.getCurrentInstance();
    }

    /**
     * @param ip {@link InjectionPoint}
     *
     * @return {@link Logger}
     */
    @Produces
    public Logger getLogger(final InjectionPoint ip)
    {
        String category = ip.getMember().getDeclaringClass().getName();

        return LoggerFactory.getLogger(category);
    }
}
