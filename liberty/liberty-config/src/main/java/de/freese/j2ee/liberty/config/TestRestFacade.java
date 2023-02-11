// Created: 25.05.2018
package de.freese.j2ee.liberty.config;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * @author Thomas Freese
 */
@Path("/")
public class TestRestFacade extends AbstractBean {
    @Resource(lookup = "java:comp/DefaultManagedExecutorService")
    private ExecutorService executorService;

    public TestRestFacade() {
        super();

        // ManagedThreadFactory threadFactory =
        // (ManagedThreadFactory) new InitialContext().lookup(
        // "java:comp/DefaultManagedThreadFactory");

        // <managedExecutorService jndiName="concurrent/executor">
        // <concurrencyPolicy max="2" maxQueueSize="3"
        // runIfQueueFull="false" maxWaitForEnqueue="0" />
        // </managedExecutorService>
    }

    /**
     * @see de.freese.j2ee.liberty.config.AbstractBean#postConstruct()
     */
    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();

        this.executorService.execute(() -> getLogger().info("postConstruct with ManagedExecutorService"));
    }

    /**
     * http://localhost:9080/config/rest/test
     */
    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public String test() throws SQLException {
        getLogger().info("test");

        return "Test";
    }
}
