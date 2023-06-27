// Created: 10.05.2013
package de.freese.j2ee.rest;

import java.util.List;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.j2ee.interceptor.MyLogging;
import de.freese.j2ee.model.Kunde;
import de.freese.j2ee.persistence.MyEntityManager;

/**
 * @author Thomas Freese
 */
@Stateless
// (name="Example", mappedName="ejb/SimpleBeanJNDI")
@Remote(de.freese.j2ee.rest.KundenService.class)
public class DefaultKundenService implements KundenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKundenService.class);

    @Inject
    @MyEntityManager
    private EntityManager entityManager;

    /**
     * @see KundenService#getData()
     */
    @SuppressWarnings("unchecked")
    @Override
    @MyLogging
    public List<Kunde> getData() {
        LOGGER.info("");

        Query query = this.entityManager.createQuery("select k from Kunde k");

        return query.getResultList();
    }
}
