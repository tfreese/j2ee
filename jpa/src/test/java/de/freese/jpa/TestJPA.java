// Created: 12.11.2015
package de.freese.jpa;

import java.util.Map;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * @author Thomas Freese
 */
class TestJPA extends AbstractTest {
    private static EntityManagerFactory entityManagerFactory;

    @AfterAll
    static void afterAll() {
        entityManagerFactory.close();
    }

    @BeforeAll
    static void beforeAll() throws Throwable {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        final Map<String, Object> config = getHibernateConfig();

        // resources/META-INF/persistence.xml
        entityManagerFactory = Persistence.createEntityManagerFactory("my.emf", config);
    }

    // private static EntityManagerFactory createEntityManagerFactory(final DataSource dataSource) {
    //     // Properties: org.hibernate.cfg.AvailableSettings
    //     // Properties config = new Properties();
    //     //
    //     // return new Configuration()
    //     //         .addProperties(config)
    //     //         .addAnnotatedClass(Employee.class)
    //     //         .buildSessionFactory();
    //
    //     if (dataSource == null) {
    //         return Persistence.createEntityManagerFactory("my.emf", Map.of(
    //                 "jakarta.persistence.jdbc.driver", "org.hsqldb.jdbc.JDBCDriver",
    //                 "jakarta.persistence.jdbc.url", "jdbc:hsqldb:mem:demo;shutdown=true",
    //                 "jakarta.persistence.jdbc.user", "sa",
    //                 "jakarta.persistence.jdbc.password", "",
    //                 "hibernate.connection.pool_size", "3"
    //         ));
    //     }
    //
    //     // Make jakarta.persistence.jdbc.url obsolete (overwrites).
    //     return Persistence.createEntityManagerFactory("my.emf", Map.of("jakarta.persistence.jtaDataSource", dataSource));
    // }

    @Override
    protected EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
