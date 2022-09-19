// Created: 14.12.2012
package de.freese.j2ee.rest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.freese.j2ee.model.Kunde;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
@LocalBean
public class StartUp
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);
    /**
     *
     */
    @PersistenceContext(unitName = "j2eeJPA")
    private EntityManager entityManager;

    /**
     *
     */
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void myPostConstruct()
    {
        StartUp.LOGGER.info("");

        Query query = this.entityManager.createQuery("select count(*) from Kunde");
        Number result = (Number) query.getSingleResult();

        if (result.intValue() == 0)
        {
            StartUp.LOGGER.info("fill DataBase");

            Kunde kunde = new Kunde();
            kunde.setName("Freese");
            kunde.setVorname("Thomas");

            this.entityManager.persist(kunde);
        }
    }

    /**
     *
     */
    @PreDestroy
    public void myPreDestroy()
    {
        LOGGER.info("");
    }
}
