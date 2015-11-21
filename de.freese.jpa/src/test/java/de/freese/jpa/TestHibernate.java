// Erzeugt: 12.11.2015
package de.freese.jpa;

import java.util.Arrays;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
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
public class TestHibernate
{
    /**
     *
     */
    private static SessionFactory SESSIONFACTORY = null;

    /**
     *
     */
    @AfterClass
    public static void afterClass()
    {
        SESSIONFACTORY.close();
    }

    /**
     *
     */
    @BeforeClass
    public static void beforeClass()
    {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        Configuration config = new Configuration();
        config.setProperty(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        // config.setProperty(AvailableSettings.DATASOURCE, "jdbc/DS");
        config.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.HSQLDialect");
        config.setProperty(AvailableSettings.DRIVER, "org.hsqldb.jdbcDriver");
        config.setProperty(AvailableSettings.URL, "jdbc:hsqldb:mem:test");
        config.setProperty(AvailableSettings.USER, "sa");
        config.setProperty(AvailableSettings.PASS, "");
        config.setProperty(AvailableSettings.FORMAT_SQL, "true");
        config.setProperty(AvailableSettings.SHOW_SQL, "true");

        // config.setProperty(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
        config.setProperty(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        config.setProperty(AvailableSettings.CACHE_REGION_PREFIX, "hibernate.test");
        config.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        config.setProperty(AvailableSettings.USE_QUERY_CACHE, "true");
        config.setProperty(AvailableSettings.GENERATE_STATISTICS, "true");
        config.setProperty(AvailableSettings.QUERY_SUBSTITUTIONS, "true 1, false 0");
        config.setProperty(AvailableSettings.FLUSH_BEFORE_COMPLETION, "true");
        config.setProperty(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, "32");
        config.setProperty(AvailableSettings.USE_REFLECTION_OPTIMIZER, "false");
        config.setProperty(AvailableSettings.STATEMENT_BATCH_SIZE, "50");
        config.setProperty(AvailableSettings.ORDER_INSERTS, "true");
        config.setProperty(AvailableSettings.ORDER_UPDATES, "false");
        config.setProperty(AvailableSettings.BATCH_VERSIONED_DATA, "true");
        config.setProperty(AvailableSettings.USE_STREAMS_FOR_BINARY, "true");
        config.setProperty(AvailableSettings.USE_MINIMAL_PUTS, "false");
        config.setProperty(AvailableSettings.USE_STRUCTURED_CACHE, "false");
        // config.setProperty(AvailableSettings.DEFAULT_SCHEMA, "...");
        // config.setProperty(AvailableSettings.SESSION_FACTORY_NAME, "myTEST");
        // config.setProperty(ENTITY_INTERCEPTOR_CLASS, ... extends org.hibernate.EmptyInterceptor);

        // org.hibernate.cfg.Environment;
        config.addAnnotatedClass(Person.class).addAnnotatedClass(Address.class);
        config.addResource("META-INF/orm.xml");

        // configures settings from hibernate.cfg.xml
        // StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

        try
        {
            SESSIONFACTORY = config.buildSessionFactory(registry);
        }
        catch (Exception ex)
        {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);

            ex.printStackTrace();
        }
    }

    /**
     *
     */
    public TestHibernate()
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
        try (Session session = SESSIONFACTORY.openSession())
        {
            session.beginTransaction();

            Person person1 = new Person("Freese", "Thomas");
            Person person2 = new Person("Freese", "Barbara");

            session.save(person1);
            session.save(person2);

            Assert.assertEquals(10L, person1.getID());
            Assert.assertEquals(11L, person2.getID());

            session.getTransaction().commit();
        }
    }

    /**
     *
     */
    @Test
    public void test2SelectAll()
    {
        try (Session session = SESSIONFACTORY.openSession())
        {
            // session.beginTransaction();

            Query query = null;
            // Caching aktiviert in Person Definition
            query = session.getNamedQuery("allPersons");
            // Caching muss explizit aktiviert werden
            // query = session.createQuery("from Person order by id asc");
            // query.setCacheable(true).setCacheRegion("person");

            List<Person> result = query.list();

            Assert.assertNotNull(result);
            Assert.assertEquals(2, result.size());

            for (Person person : result)
            {
                System.out.println(person);
            }

            // session.getTransaction().commit();
        }
    }

    /**
     *
     */
    @Test
    public void test3SelectVorname()
    {
        String param = "Thomas";

        try (Session session = SESSIONFACTORY.openSession())
        {
            // session.beginTransaction();

            Query query = null;
            // Caching aktiviert in Person Definition
            query = session.getNamedQuery("personByVorname");
            // Caching muss explizit aktiviert werden
            // query = session.createQuery("from Person where vorname=:vorname order by name asc");
            // query.setCacheable(true).setCacheRegion("person");

            query.setString("vorname", param);

            List<Person> result = query.list();

            Assert.assertNotNull(result);
            Assert.assertEquals(1, result.size());

            Person person = result.get(0);
            Assert.assertEquals(param, person.getVorName());
            Assert.assertEquals(10, person.getID());

            System.out.println(person);

            // session.getTransaction().commit();
        }
    }

    /**
     *
     */
    @Test
    public void test4Statistics()
    {
        Statistics stats = SESSIONFACTORY.getStatistics();

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
