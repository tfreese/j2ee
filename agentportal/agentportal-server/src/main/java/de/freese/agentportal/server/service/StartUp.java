// Created: 14.12.2012
package de.freese.agentportal.server.service;

import java.util.Date;

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

import de.freese.agentportal.common.model.SecretNews;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
@LocalBean
public class StartUp {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);

    // @Inject
    // @AgentPortalEm
    @PersistenceContext(unitName = "agentPortalJPA")
    private EntityManager em;

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void myPostConstruct() {
        LOGGER.info("");

        Query query = this.em.createQuery("select count(n.id) from SecretNews n");
        Number result = (Number) query.getSingleResult();

        if (result.intValue() > 0) {
            return;
        }

        LOGGER.info("fill DataBase");

        Date today = new Date();

        SecretNews entity = new SecretNews();
        entity.setSecurityLevel(SecretNews.SECURITY_LEVEL_LOW);
        entity.setTitle("Johnny English verhaftet");
        entity.setTimestamp(today);
        entity.setText("Unser Agent Johnny English wurde wieder einmal bei einer geheimen Mission festgenommen. Weitere Informationen sind nicht bekannt und folgen demnächst.");
        this.em.persist(entity);

        // if (this.em.contains(entity))
        // {
        // this.em.merge(entity);
        // }
        // else
        // {
        // this.em.persist(entity);
        // }

        entity = new SecretNews();
        entity.setSecurityLevel(SecretNews.SECURITY_LEVEL_HIGH);
        entity.setTitle("James Bond pensioniert");
        entity.setTimestamp(today);
        entity.setText("Unser Agent James Bond wurde mit dem heutigen Tag in Pension geschickt. In unzähligen Missionen stand er seinen Mann, um sein Vaterland zu verteidigen.");
        this.em.persist(entity);
    }

    @PreDestroy
    public void myPreDestroy() {
        LOGGER.info("");
    }
}
