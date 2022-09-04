// Created: 10.05.2013
package de.freese.j2ee.rest;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.freese.j2ee.interceptor.MyLogging;
import de.freese.j2ee.model.Kunde;
import de.freese.j2ee.persistence.MyEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Stateless
// (name="Example", mappedName="ejb/SimpleBeanJNDI")
@Remote(KundenService.class)
public class DefaultKundenService implements KundenService
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKundenService.class);
    /**
     *
     */
    @Inject
    @MyEntityManager
    private EntityManager entityManager;

    /**
     * @see KundenService#getData()
     */
    @SuppressWarnings("unchecked")
    @Override
    @MyLogging
    public List<Kunde> getData()
    {
        LOGGER.info("");

        Query query = this.entityManager.createQuery("select k from Kunde k");

        return query.getResultList();
    }
}
