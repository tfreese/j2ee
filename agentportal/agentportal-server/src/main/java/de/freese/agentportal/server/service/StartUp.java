// Created: 14.12.2012
package de.freese.agentportal.server.service;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.freese.agentportal.common.model.SecretNews;
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
    // @Inject
    // @AgentPortalEM
    @PersistenceContext(unitName = "agentPortalJPA")
    private EntityManager em;

    /**
     *
     */
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void myPostConstruct()
    {
        LOGGER.info("");

        Query query = this.em.createQuery("select count(n.id) from SecretNews n");
        Number result = (Number) query.getSingleResult();

        if (result.intValue() > 0)
        {
            return;
        }

        LOGGER.info("fill DataBase");

        Date today = new Date();

        SecretNews entity = new SecretNews();
        entity.setSecuritylevel(SecretNews.SECURITY_LEVEL_LOW);
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
        entity.setSecuritylevel(SecretNews.SECURITY_LEVEL_HIGH);
        entity.setTitle("James Bond pensioniert");
        entity.setTimestamp(today);
        entity.setText("Unser Agent James Bond wurde mit dem heutigen Tag in Pension geschickt. In unzähligen Missionen stand er seinen Mann, um sein Vaterland zu verteidigen.");
        this.em.persist(entity);
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
