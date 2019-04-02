// Erzeugt: 12.11.2015
package de.freese.jpa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import de.freese.jpa.model.Address;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestJPA extends AbstractTest
{
    /**
     *
     */
    private static EntityManagerFactory ENTITYMANAGERFACTORY = null;

    /**
     *
     */
    @AfterAll
    static void afterAll()
    {
        ENTITYMANAGERFACTORY.close();
    }

    /**
     *
     */
    @BeforeAll
    static void beforeAll()
    {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        Properties properties = getHibernateProperties();
        Map<String, Object> config = new HashMap<>();

        properties.keySet().forEach(key -> {
            config.put((String) key, properties.getProperty((String) key));
        });

        // resources/META-INF/persistence.xml
        try
        {
            ENTITYMANAGERFACTORY = Persistence.createEntityManagerFactory("de.freese.test", config);
        }
        catch (Exception ex)
        {
            LOGGER.error(null, ex);
        }
    }

    /**
     *
     */
    public TestJPA()
    {
        super();
    }

    /**
     * @see de.freese.jpa.AbstractTest#test1Insert()
     */
    @Override
    @Test
    public void test1Insert()
    {
        EntityManager entityManager = ENTITYMANAGERFACTORY.createEntityManager();
        entityManager.getTransaction().begin();

        List<Person> persons = createPersons();
        persons.forEach(person -> {
            entityManager.persist(person);
        });

        validateTest1Insert(persons);

        entityManager.flush(); // ohne flush kein insert
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * @see de.freese.jpa.AbstractTest#test2SelectAll()
     */
    @SuppressWarnings("unchecked")
    @Override
    @Test
    public void test2SelectAll()
    {
        EntityManager entityManager = ENTITYMANAGERFACTORY.createEntityManager();
        // entityManager.getTransaction().begin();

        Query query = null;
        // Caching aktiviert in Person Definition
        query = entityManager.createNamedQuery("allPersons");
        // Caching muss explizit aktiviert werden
        // query = entityManager.createQuery("from Person order by id asc");
        // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person");

        List<Person> persons = query.getResultList();

        validateTest2SelectAll(persons);

        // entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * @see de.freese.jpa.AbstractTest#test3SelectVorname()
     */
    @Override
    @Test
    public void test3SelectVorname()
    {
        String vorname = "Vorname1";

        EntityManager entityManager = ENTITYMANAGERFACTORY.createEntityManager();
        // entityManager.getTransaction().begin();

        Query query = null;
        // Caching aktiviert in Person Definition
        query = entityManager.createNamedQuery("personByVorname");
        // Caching muss explizit aktiviert werden
        // query = entityManager.createQuery("from Person where vorname=:vorname order by name asc");
        // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person");

        query.setParameter("vorname", vorname);

        Person person = (Person) query.getSingleResult();

        validateTest3SelectVorname(Arrays.asList(person), vorname);

        // entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * @see de.freese.jpa.AbstractTest#test4NativeQuery()
     */
    @SuppressWarnings("unchecked")
    @Override
    @Test
    public void test4NativeQuery()
    {
        List<Person> persons = new ArrayList<>();

        EntityManager entityManager = ENTITYMANAGERFACTORY.createEntityManager();
        // java.sql.Connection connection = entityManager.unwrap(java.sql.Connection.class);

        // entityManager.getTransaction().begin();
        // !!! Aliase funktionieren bei Native-Queries ohne Mappingobjekt nicht !!!
        // !!! Scalare Werte (addScalar) wie in Hibernate funktionieren bei JPA nicht !!!
        // !!! Kein Caching bei Native-Queries !!!
        Query query = entityManager.createNamedQuery("allPersons.native");
        // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person");

        List<Object[]> rows = query.getResultList();
        rows.forEach(row -> {
            Person person = new Person((String) row[1], (String) row[2]);
            person.setID(((BigInteger) row[0]).longValue());

            persons.add(person);
        });

        query = entityManager.createNativeQuery("select id, street from T_ADDRESS where person_id = :person_id order by street desc");
        // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "address");

        for (Person person : persons)
        {
            query.setParameter("person_id", person.getID());
            rows = query.getResultList();
            rows.forEach(row -> {
                Address address = new Address((String) row[1]);
                address.setID(((BigInteger) row[0]).longValue());

                person.addAddress(address);
            });
        }

        validateTest2SelectAll(persons);

        // entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     *
     */
    @Test
    public void test99Statistics()
    {
        dumpStatistics(System.out, (SessionFactoryImpl) ENTITYMANAGERFACTORY);
    }
}
