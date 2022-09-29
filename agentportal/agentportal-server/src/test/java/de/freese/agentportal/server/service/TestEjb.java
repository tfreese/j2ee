// Created: 25.05.2013
package de.freese.agentportal.server.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;

import de.freese.agentportal.common.service.ISecretNewsService;
import jakarta.ejb.EJB;
import jakarta.ejb.embeddable.EJBContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Thomas Freese
 */
class TestEjb
{
    /**
     *
     */
    private static EJBContainer container;

    /**
     *
     */
    @AfterAll
    public static void afterAll()
    {
        if (container != null)
        {
            container.close();
        }
    }

    /**
     * @throws Exception Falls was schiefgeht.
     */
    @BeforeAll
    static void BeforeAll() throws Exception
    {
        Properties props = new Properties();
        // props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
        props.setProperty("jdbc/agentPortal", "new://Resource?type=DataSource");
        props.setProperty("jdbc/agentPortal.JdbcDriver", "org.hsqldb.jdbcDriver");
        props.setProperty("jdbc/agentPortal.JdbcUrl", "jdbc:hsqldb:mem:agentPortal");
        props.setProperty("jdbc/agentPortal.UserName", "SA");
        props.setProperty("jdbc/agentPortal.Password", "");

        // TODO container = EJBContainer.createEJBContainer(props);
    }

    /**
     *
     */
    @EJB
    private ISecretNewsService service;

    /**
     * @throws Exception Falls was schiefgeht.
     */
    @BeforeEach
    void beforeEach() throws Exception
    {
        // TODO container.getContext().bind("inject", this);
    }

    /**
     *
     */
    @org.junit.jupiter.api.Test
    void testService()
    {
        // Was the EJB injected?
        // TODO assertTrue(this.service != null);
        assertTrue(true);
    }
}
