// Created: 14.12.2012
package de.freese.j2ee.rest;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.j2ee.model.Kunde;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
@LocalBean
public class StartUp {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);

    @PersistenceContext(unitName = "j2eeJPA")
    private EntityManager entityManager;

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void myPostConstruct() {
        StartUp.LOGGER.info("");

        final Query query = this.entityManager.createQuery("select count(*) from Kunde");
        final Number result = (Number) query.getSingleResult();

        if (result.intValue() == 0) {
            StartUp.LOGGER.info("fill DataBase");

            final Kunde kunde = new Kunde();
            kunde.setName("Freese");
            kunde.setVorname("Thomas");

            this.entityManager.persist(kunde);
        }
    }

    @PreDestroy
    public void myPreDestroy() {
        LOGGER.info("");
    }
}
