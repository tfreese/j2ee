// Created: 12.11.2015
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.freese.jpa.model.Address;
import de.freese.jpa.model.MyProjectionVo;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("unchecked")
class TestJPA extends AbstractTest {
    private static EntityManagerFactory entityManagerFactory;

    @AfterAll
    static void afterAll() {
        entityManagerFactory.close();
    }

    @BeforeAll
    static void beforeAll() {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        final Map<String, Object> config = getHibernateConfig();

        // resources/META-INF/persistence.xml
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("de.freese.test", config);
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    @Test
    public void test010Insert() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();

            final List<Person> persons = createPersons();
            persons.forEach(entityManager::persist);

            validateTest1Insert(persons);

            //            entityManager.flush(); // without no flush -> no insert
            entityManager.getTransaction().commit();
        }
    }

    @Override
    @Test
    public void test020SelectAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            // entityManager.getTransaction().begin();

            // Caching must be enabled explicitly.
            // final List<Person> persons = entityManager.createQuery("from Person order by id asc")
            // .setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person").getResultList();

            // Caching is enabled in Mapping.
            final List<Person> persons = entityManager.createNamedQuery("allPersons", Person.class).getResultList();

            validateTest2SelectAll(persons);

            // entityManager.getTransaction().commit();
        }
    }

    @Override
    @Test
    public void test030SelectVorname() {
        final String vorname = "Vorname1";

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            // entityManager.getTransaction().begin();

            // Caching is enabled in Mapping.
            final Person person = entityManager.createNamedQuery("personByVorname", Person.class).setParameter("vorname", vorname).getSingleResult();

            // Caching must be enabled explicitly.
            // final Person person = entityManager.createQuery("from Person where vorname=:vorname order by name asc", Person.class)
            // setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person").getSingleResult();

            validateTest3SelectVorname(Arrays.asList(person), vorname);

            // entityManager.getTransaction().commit();
        }
    }

    @Override
    @Test
    public void test040NativeQuery() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            // final Connection connection = entityManager.unwrap(Connection.class);
            // final Session session = entityManager.unwrap(Session.class);

            // entityManager.getTransaction().begin();
            // !!! Aliases won't work in Native-Queries without Mapping object !!!
            // !!! Scalar Values (addScalar) like in Hibernate are not working for JPA !!!
            // !!! No Caching for Named-Queries !!!
            final List<Person> persons = entityManager.createNamedQuery("allPersons.native", Object[].class)
                    //            .setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person")
                    .getResultStream().map(row -> {
                        final Person person = new Person((String) row[1], (String) row[2]);
                        person.setID((long) row[0]);
                        return person;
                    }).toList();

            assertNotNull(persons);
            assertFalse(persons.isEmpty());

            final Query query = entityManager.createNativeQuery("select id, street from T_ADDRESS where person_id = :personId order by street desc");
            // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "address");

            for (Person person : persons) {
                // query.setParameter("personId", person.getID()).getResultStream().map(Object[].class::cast).map(row -> {
                //     final Address address = new Address((String) row[1]);
                //     address.setID((long) row[0]);
                //     return address;
                // }).forEach(person::addAddress);

                final List<Object[]> addresses = query.setParameter("personId", person.getID()).getResultList();

                assertNotNull(addresses);
                assertFalse(addresses.isEmpty());

                addresses.forEach(row -> {
                    final Address address = new Address((String) row[1]);
                    address.setID((long) row[0]);

                    person.addAddress(address);
                });
            }

            validateTest2SelectAll(persons);

            // entityManager.getTransaction().commit();
        }
    }

    @Test
    void test060Projection() {
        assertInstanceOf(SessionFactory.class, entityManagerFactory.unwrap(SessionFactory.class));

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            assertInstanceOf(Session.class, entityManager.unwrap(Session.class));

            // entityManager.getTransaction().begin();

            final StringBuilder hql = new StringBuilder();
            hql.append("select");
            hql.append(" new de.freese.jpa.model.MyProjectionVo");
            hql.append(" (");
            hql.append("p.id");
            hql.append(", p.name");
            hql.append(")");
            hql.append(" from Person p");
            hql.append(" order by p.name asc");

            final List<MyProjectionVo> result = entityManager.createQuery(hql.toString(), MyProjectionVo.class).getResultList();

            assertNotNull(result);
            assertFalse(result.isEmpty());

            for (int i = 1; i <= result.size(); i++) {
                final MyProjectionVo dto = result.get(i - 1);

                assertEquals("Name" + i, dto.getName());
            }

            // entityManager.getTransaction().commit();
        }
    }

    @Test
    void test99Statistics() {
        dumpStatistics(System.out, entityManagerFactory.unwrap(SessionFactory.class));

        assertTrue(true);
    }
}
