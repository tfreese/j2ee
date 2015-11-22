// Erzeugt: 12.11.2015
package de.freese.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJPA extends AbstractTest
{
    /**
     *
     */
    private static EntityManagerFactory ENTITYMANAGERFACTORY = null;

    /**
     *
     */
    @AfterClass
    public static void afterClass()
    {
        ENTITYMANAGERFACTORY.close();
    }

    /**
     *
     */
    @BeforeClass
    public static void beforeClass()
    {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        Properties properties = getHibernateProperties();
        Map<String, Object> config = new HashMap<>();

        for (Object key : properties.keySet())
        {
            config.put((String) key, properties.getProperty((String) key));
        }

        // resources/META-INF/persistence.xml
        try
        {
            ENTITYMANAGERFACTORY = Persistence.createEntityManagerFactory("de.efreest.test", config);
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
     *
     */
    @After
    public void afterMethod()
    {
        // TODO
    }

    /**
     *
     */
    @Before
    public void beforeMethod()
    {
        // TODO
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

        for (Person person : persons)
        {
            entityManager.persist(person);
        }

        validateTest1Insert(persons);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * @see de.freese.jpa.AbstractTest#test2SelectAll()
     */
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
        // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE);
        // //query.setHint(QueryHints.CACHE_REGION, "person");

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
        // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE);
        // //query.setHint(QueryHints.CACHE_REGION, "person");

        query.setParameter("vorname", vorname);

        List<Person> persons = query.getResultList();

        validateTest3SelectVorname(persons, vorname);

        // entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     *
     */
    @Test
    public void test4Statistics()
    {
        dumpStatistics(System.out, ((EntityManagerFactoryImpl) ENTITYMANAGERFACTORY).getSessionFactory());
    }
}
