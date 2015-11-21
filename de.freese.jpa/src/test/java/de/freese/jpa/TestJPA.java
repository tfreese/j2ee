// Erzeugt: 12.11.2015
package de.freese.jpa;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import de.freese.jpa.model.Address;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJPA
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

        Map<String, Object> config = new HashMap<>();
        config.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        // config.setProperty(AvailableSettings.DATASOURCE, "jdbc/DS");
        config.put(AvailableSettings.DIALECT, "org.hibernate.dialect.HSQLDialect");
        config.put(AvailableSettings.DRIVER, "org.hsqldb.jdbcDriver");
        config.put(AvailableSettings.URL, "jdbc:hsqldb:mem:test");
        config.put(AvailableSettings.USER, "sa");
        config.put(AvailableSettings.PASS, "");
        config.put(AvailableSettings.FORMAT_SQL, "true");
        config.put(AvailableSettings.SHOW_SQL, "true");

        // config.put(AvailableSettings.CACHE_REGION_FACTORY,"org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
        config.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        config.put(AvailableSettings.CACHE_REGION_PREFIX, "hibernate.test");
        config.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        config.put(AvailableSettings.USE_QUERY_CACHE, "true");
        config.put(AvailableSettings.GENERATE_STATISTICS, "true");
        config.put(AvailableSettings.QUERY_SUBSTITUTIONS, "true 1, false 0");
        config.put(AvailableSettings.FLUSH_BEFORE_COMPLETION, "true");
        config.put(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, "32");
        config.put(AvailableSettings.USE_REFLECTION_OPTIMIZER, "false");
        config.put(AvailableSettings.STATEMENT_BATCH_SIZE, "50");
        config.put(AvailableSettings.ORDER_INSERTS, "true");
        config.put(AvailableSettings.ORDER_UPDATES, "false");
        config.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");
        config.put(AvailableSettings.USE_STREAMS_FOR_BINARY, "true");
        config.put(AvailableSettings.USE_MINIMAL_PUTS, "false");
        config.put(AvailableSettings.USE_STRUCTURED_CACHE, "false");
        // config.put(AvailableSettings.DEFAULT_SCHEMA, "...");
        // config.put(AvailableSettings.SESSION_FACTORY_NAME, "myTEST");
        // config.put(AvailableSettings.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName());
        // config.put(ENTITY_INTERCEPTOR_CLASS, ... extends org.hibernate.EmptyInterceptor);

        // resources/META-INF/persistence.xml
        try
        {
            ENTITYMANAGERFACTORY = Persistence.createEntityManagerFactory("de.efreest.test", config);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
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
     *
     */
    @Test
    public void test1Insert()
    {
        EntityManager entityManager = ENTITYMANAGERFACTORY.createEntityManager();
        entityManager.getTransaction().begin();

        Person person1 = new Person("Freese", "Thomas");
        Person person2 = new Person("Freese", "Barbara");

        entityManager.persist(person1);
        entityManager.persist(person2);

        Assert.assertEquals(10, person1.getID());
        Assert.assertEquals(11, person2.getID());

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     *
     */
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

        List<Person> result = query.getResultList();

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());

        for (Person person : result)
        {
            System.out.println(person);
        }

        // entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     *
     */
    @Test
    public void test3SelectVorname()
    {
        String param = "Thomas";

        EntityManager entityManager = ENTITYMANAGERFACTORY.createEntityManager();
        // entityManager.getTransaction().begin();

        Query query = null;
        // Caching aktiviert in Person Definition
        query = entityManager.createNamedQuery("personByVorname");
        // Caching muss explizit aktiviert werden
        // query = entityManager.createQuery("from Person where vorname=:vorname order by name asc");
        // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE);
        // //query.setHint(QueryHints.CACHE_REGION, "person");

        query.setParameter("vorname", param);

        List<Person> result = query.getResultList();

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());

        Person person = result.get(0);
        Assert.assertEquals(param, person.getVorName());
        Assert.assertEquals(10, person.getID());

        System.out.println(person);

        // entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     *
     */
    @Test
    public void test4Statistics()
    {
        Statistics stats = ((EntityManagerFactoryImpl) ENTITYMANAGERFACTORY).getSessionFactory().getStatistics();

        long txCount = stats.getTransactionCount();
        long successfulTxCount = stats.getSuccessfulTransactionCount();

        System.out.println("PreparedStatement Count : " + stats.getPrepareStatementCount());
        System.out.println("Session open Count......: " + stats.getSessionOpenCount());
        System.out.println("Session close Count.....: " + successfulTxCount);
        System.out.println("Begin Transaction Count : " + txCount);
        System.out.println("Commit Transaction Count: " + stats.getSuccessfulTransactionCount());

        // Globaler 2nd lvl Cache
        double hitCount = stats.getSecondLevelCacheHitCount();
        double missCount = stats.getSecondLevelCacheMissCount();
        double hitRatio = hitCount / (hitCount + missCount);
        System.out.println("Second Cache Hit Count...: " + hitCount);
        System.out.println("Second Cache Miss Count..: " + missCount);
        System.out.println("Second Cache Hit ratio[%]: " + (hitRatio * 100));

        // Globaler Query Cache
        hitCount = stats.getQueryCacheHitCount();
        missCount = stats.getQueryCacheMissCount();
        hitRatio = hitCount / (hitCount + missCount);
        System.out.println("SQL Query Hit Count: " + hitCount);
        System.out.println("SQL Query Miss Count: " + missCount);
        System.out.println("SQL Query Hit ratio %: " + (hitRatio * 100));

        String[] cacheRegions = stats.getSecondLevelCacheRegionNames();
        Arrays.sort(cacheRegions);

        for (String cacheRegion : cacheRegions)
        {
            SecondLevelCacheStatistics cacheStatistics = stats.getSecondLevelCacheStatistics(cacheRegion);

            hitCount = cacheStatistics.getHitCount();
            missCount = cacheStatistics.getMissCount();
            hitRatio = hitCount / (hitCount + missCount);

            System.out.println("Cache Region.........: " + cacheRegion);
            System.out.println("Objects in Memory....: " + cacheStatistics.getElementCountInMemory());
            System.out.println("Objects in Memory[MB]: " + (cacheStatistics.getSizeInMemory() / 1024D / 1024D));
            System.out.println("Hit Count............: " + hitCount);
            System.out.println("Miss Count...........: " + missCount);
            System.out.println("Hit ratio[%].........: " + (hitRatio * 100));
            System.out.println();
        }

        // Objektspezifische Statistiken
        // Map<String, ?> metaData = sessionFactory.getAllClassMetadata();
        // Set<String> clazzes = metaData.keySet();
        Class<?>[] clazzes = new Class<?>[]
                {
                Person.class, Address.class
                };

        for (Class<?> clazz : clazzes)
        {
            String className = clazz.getName();

            EntityStatistics entityStats = stats.getEntityStatistics(className);
            long inserts = entityStats.getInsertCount();
            long updates = entityStats.getUpdateCount();
            long deletes = entityStats.getDeleteCount();
            long fetches = entityStats.getFetchCount();
            long loads = entityStats.getLoadCount();
            long changes = inserts + updates + deletes;

            System.out.println(className + " fetches " + fetches + " times");
            System.out.println(className + " loads " + loads + " times");
            System.out.println(className + " inserts " + inserts + " times");
            System.out.println(className + " updates " + updates + " times");
            System.out.println(className + " deletes " + deletes + " times");
            System.out.println(className + " changed " + changes + " times");
        }
    }
}
