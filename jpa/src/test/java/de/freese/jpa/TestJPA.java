// Created: 12.11.2015
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import de.freese.jpa.model.Address;
import de.freese.jpa.model.MyProjectionDTO;
import de.freese.jpa.model.Person;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestJPA extends AbstractTest
{
    private static EntityManagerFactory entityManagerFactory;

    @AfterAll
    static void afterAll()
    {
        entityManagerFactory.close();
    }

    @BeforeAll
    static void beforeAll()
    {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        Map<String, Object> config = getHibernateConfig();

        // resources/META-INF/persistence.xml
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("de.freese.test", config);
        }
        catch (Exception ex)
        {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test010Insert()
     */
    @Override
    @Test
    public void test010Insert()
    {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager())
        {
            entityManager.getTransaction().begin();

            List<Person> persons = createPersons();
            persons.forEach(entityManager::persist);

            validateTest1Insert(persons);

            entityManager.flush(); // ohne flush kein insert
            entityManager.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test020SelectAll()
     */
    @Override
    @Test
    public void test020SelectAll()
    {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager())
        {
            // entityManager.getTransaction().begin();

            Query query;
            // Caching aktiviert in Person Definition
            query = entityManager.createNamedQuery("allPersons");
            // Caching muss explizit aktiviert werden
            // query = entityManager.createQuery("from Person order by id asc");
            // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person");

            List<Person> persons = query.getResultList();

            validateTest2SelectAll(persons);

            // entityManager.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test030SelectVorname()
     */
    @Override
    @Test
    public void test030SelectVorname()
    {
        String vorname = "Vorname1";

        try (EntityManager entityManager = entityManagerFactory.createEntityManager())
        {
            // entityManager.getTransaction().begin();

            Query query;
            // Caching aktiviert in Person Definition
            query = entityManager.createNamedQuery("personByVorname");
            // Caching muss explizit aktiviert werden
            // query = entityManager.createQuery("from Person where vorname=:vorname order by name asc");
            // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person");

            query.setParameter("vorname", vorname);

            Person person = (Person) query.getSingleResult();

            validateTest3SelectVorname(Arrays.asList(person), vorname);

            // entityManager.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test040NativeQuery()
     */
    @Override
    @Test
    public void test040NativeQuery()
    {
        List<Person> persons = new ArrayList<>();

        try (EntityManager entityManager = entityManagerFactory.createEntityManager())
        {
            // java.sql.Connection connection = entityManager.unwrap(java.sql.Connection.class);

            // entityManager.getTransaction().begin();
            // !!! Aliase funktionieren bei Native-Queries ohne Mappingobjekt nicht !!!
            // !!! Scalar Werte (addScalar) wie in Hibernate funktionieren bei JPA nicht !!!
            // !!! Kein Caching bei Native-Queries !!!
            TypedQuery<Object[]> typedQuery = entityManager.createNamedQuery("allPersons.native", Object[].class);
            // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person");

            List<Object[]> rows = typedQuery.getResultList();
            rows.forEach(row ->
            {
                Person person = new Person((String) row[1], (String) row[2]);
                person.setID((long) row[0]);

                persons.add(person);
            });

            Query query = entityManager.createNativeQuery("select id, street from T_ADDRESS where person_id = :personId order by street desc");
            // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "address");

            for (Person person : persons)
            {
                query.setParameter("personId", person.getID());
                rows = query.getResultList();
                rows.forEach(row ->
                {
                    Address address = new Address((String) row[1]);
                    address.setID((long) row[0]);

                    person.addAddress(address);
                });
            }

            validateTest2SelectAll(persons);

            // entityManager.getTransaction().commit();
        }
    }

    @Test
    void test6Projection()
    {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager())
        {
            // entityManager.getTransaction().begin();

            StringBuilder hql = new StringBuilder();
            hql.append("select");
            hql.append(" new de.freese.jpa.model.MyProjectionDTO(");
            hql.append("p.id");
            hql.append(", p.name");
            hql.append(")");
            hql.append(" from Person p");
            hql.append(" order by p.name asc");

            TypedQuery<MyProjectionDTO> query = entityManager.createQuery(hql.toString(), MyProjectionDTO.class);
            List<MyProjectionDTO> result = query.getResultList();

            assertNotNull(result);
            assertFalse(result.isEmpty());

            for (int i = 1; i <= result.size(); i++)
            {
                MyProjectionDTO dto = result.get(i - 1);

                assertEquals("Name" + i, dto.getName());
            }

            // entityManager.getTransaction().commit();
        }
    }

    @Test
    void test99Statistics()
    {
        dumpStatistics(System.out, (SessionFactoryImpl) entityManagerFactory);

        assertTrue(true);
    }
}
