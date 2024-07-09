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
        entityManagerFactory = Persistence.createEntityManagerFactory("de.freese.test", config);
    }

    @Override
    protected EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
