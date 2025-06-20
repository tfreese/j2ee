// Created: 14.12.2012
package de.freese.liberty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.liberty.model.Person;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
// @LocalBean
public class StartUp {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);

    @Resource(lookup = "jdbc/dbDS")
    private DataSource dataSource;

    // @jakarta.ws.rs.core.Context
    // private SecurityContext context;

    @PersistenceContext(unitName = "my-pu")
    private EntityManager entityManager;

    @Resource(lookup = "java:comp/DefaultManagedExecutorService")
    private ExecutorService executorService;

    @Resource(lookup = "java:comp/DefaultManagedScheduledExecutorService")
    private ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void myPostConstruct() {
        LOGGER.info("myPostConstruct");

        executorService.execute(() -> LOGGER.info("postConstruct with DefaultManagedExecutorService"));
        scheduledExecutorService.execute(() -> LOGGER.info("postConstruct with DefaultManagedScheduledExecutorService"));

        final AtomicReference<String> databaseName = new AtomicReference<>("");

        // try (Session session = entityManager.unwrap(Session.class)) {
        final Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            databaseName.set(connection.getMetaData().getDatabaseProductName());
            LOGGER.info(databaseName.get());
        });
        // }
        // catch (Exception ex) {
        //     LOGGER.error(ex.getMessage());
        // }

        if (databaseName.get().contains("HSQL")) {
            // HSQLDB; SELECT COUNT(*) FROM INFORMATION_SCHEMA.SYSTEM_USERS
            queryDateTime(dataSource, "VALUES (CURRENT_TIMESTAMP)");
        }
        else {
            // H2; SELECT 1
            // queryDateTime(dataSource, "select CURRENT_TIMESTAMP");
        }

        final Long result = entityManager.createQuery("select count(*) from Person", Long.class).getSingleResult();

        if (result == 0L) {
            LOGGER.info("fill DataBase");

            final Person person = new Person();
            person.setName("Freese");
            entityManager.persist(person);
        }
    }

    @PreDestroy
    public void myPreDestroy() {
        LOGGER.info("myPreDestroy");
    }

    private void queryDateTime(final DataSource dataSource, final String sql) {
        try {
            final LocalDateTime localDateTime;

            try (Connection con = dataSource.getConnection();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                rs.next();

                // localDateTime = rs.getTimestamp(1).toLocalDateTime();
                localDateTime = rs.getObject(1, LocalDateTime.class);
            }

            LOGGER.info("Database LocalDateTime: {}", localDateTime);
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
