// Created: 12.11.2015
package de.freese.jpa;

import java.util.Map;

import jakarta.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import de.freese.jpa.model.Address;
import de.freese.jpa.model.Person;

/**
 * SessionFactory extends EntityManagerFactory<br>
 * Session extends EntityManager
 *
 * @author Thomas Freese
 */
class TestHibernate extends AbstractTest {
    private static SessionFactory sessionFactory;

    @AfterAll
    static void afterAll() {
        sessionFactory.close();
    }

    @BeforeAll
    static void beforeAll() throws Throwable {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        final Map<String, Object> config = getHibernateConfig();

        sessionFactory = new Configuration()
                // .addProperties(...)
                .addPackage("de.freese.jpa.model") // for package-info.java
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Address.class)
                // .addResource("META-INF/orm.xml")
                .buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(config).build());

        // hibernate.properties required.
        // sessionFactory = new Configuration()
        //         .addAnnotatedClass(Person.class)
        //         .addAnnotatedClass(Address.class)
        //         .buildSessionFactory(new StandardServiceRegistryBuilder().build());

        // final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config).build();
        //
        // final MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        // metadataSources
        //         .addPackage("de.freese.jpa.model") // for package-info.java
        //         .addAnnotatedClass(Person.class)
        //         .addAnnotatedClass(Address.class)
        // // .addResource("META-INF/orm.xml")
        // ;
        //
        // sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
        //
        // // The registry would be destroyed by the SessionFactory, but we have trouble building the SessionFactory so destroy it manually.
        // // StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }

    @Override
    protected EntityManagerFactory getEntityManagerFactory() {
        return sessionFactory;
    }
}
