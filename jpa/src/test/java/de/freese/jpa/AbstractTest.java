/**
 * Created: 22.11.2015
 */
package de.freese.jpa;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.persistence.metamodel.Metamodel;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.freese.jpa.model.Address;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
public abstract class AbstractTest
{
    /**
     *
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger("TestLogger");

    /**
     * @return {@link Properties}
     */
    protected static Properties getHibernateProperties()
    {
        // org.hibernate.cfg.Environment;
        Properties config = new Properties();

        // Connection Properties
        // config.setProperty(AvailableSettings.DATASOURCE, "jdbc/DS");
        config.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.HSQLDialect");
        config.setProperty(AvailableSettings.DRIVER, "org.hsqldb.jdbc.JDBCDriver");
        config.setProperty(AvailableSettings.URL, "jdbc:hsqldb:mem:test");
        config.setProperty(AvailableSettings.USER, "sa");
        config.setProperty(AvailableSettings.PASS, "");

        // Schema erzeugen
        config.setProperty(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        // config.setProperty(AvailableSettings.HBM2DDL_IMPORT_FILES, "import.sql");

        // SQL-Format
        config.setProperty(AvailableSettings.FORMAT_SQL, Boolean.toString(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()));
        config.setProperty(AvailableSettings.SHOW_SQL, Boolean.toString(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()));
        config.setProperty(AvailableSettings.GENERATE_STATISTICS,
                Boolean.toString(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()));

        // Caching
        // config.setProperty(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
        config.setProperty(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        config.setProperty(AvailableSettings.CACHE_REGION_PREFIX, "hibernate.test");
        config.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        config.setProperty(AvailableSettings.USE_QUERY_CACHE, "true");

        // Sonstiges
        // config.setProperty(AvailableSettings.DEFAULT_SCHEMA, "...");
        // config.setProperty(AvailableSettings.SESSION_FACTORY_NAME, "myTEST");
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

        // config.setProperty(ENTITY_INTERCEPTOR_CLASS, "... extends org.hibernate.EmptyInterceptor");
        return config;
    }

    /**
     * Erstellt ein neues {@link AbstractTest} Object.
     */
    public AbstractTest()
    {
        super();
    }

    /**
     * @return {@link Person}
     */
    protected List<Person> createPersons()
    {
        List<Person> persons = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
        {
            Person person = new Person("Name" + i, "Vorname" + i);
            persons.add(person);

            for (int j = 1; j <= 3; j++)
            {
                Address address = new Address("Street" + i + "" + j);
                person.addAddress(address);
            }
        }

        return persons;
    }

    /**
     * @param ps {@link PrintStream}
     * @param sessionFactory {@link SessionFactory}
     */
    protected void dumpStatistics(final PrintStream ps, final SessionFactory sessionFactory)
    {
        if (System.out == ps)
        {
            // Erst mal deaktiviert
            return;
        }

        Statistics stats = sessionFactory.getStatistics();

        long txCount = stats.getTransactionCount();
        long successfulTxCount = stats.getSuccessfulTransactionCount();

        ps.println("PreparedStatement Count : " + stats.getPrepareStatementCount());
        ps.println("Session open Count......: " + stats.getSessionOpenCount());
        ps.println("Session close Count.....: " + successfulTxCount);
        ps.println("Begin Transaction Count : " + txCount);
        ps.println("Commit Transaction Count: " + stats.getSuccessfulTransactionCount());

        // Globaler 2nd lvl Cache
        double hitCount = stats.getSecondLevelCacheHitCount();
        double missCount = stats.getSecondLevelCacheMissCount();
        double hitRatio = hitCount / (hitCount + missCount);
        ps.println("Second Cache Hit Count...: " + hitCount);
        ps.println("Second Cache Miss Count..: " + missCount);
        ps.println("Second Cache Hit ratio[%]: " + (hitRatio * 100));

        // Globaler Query Cache
        hitCount = stats.getQueryCacheHitCount();
        missCount = stats.getQueryCacheMissCount();
        hitRatio = hitCount / (hitCount + missCount);
        ps.println("SQL Query Hit Count: " + hitCount);
        ps.println("SQL Query Miss Count: " + missCount);
        ps.println("SQL Query Hit ratio %: " + (hitRatio * 100));

        String[] cacheRegions = stats.getSecondLevelCacheRegionNames();
        Arrays.sort(cacheRegions);

        for (String cacheRegion : cacheRegions)
        {
            SecondLevelCacheStatistics cacheStatistics = stats.getSecondLevelCacheStatistics(cacheRegion);

            hitCount = cacheStatistics.getHitCount();
            missCount = cacheStatistics.getMissCount();
            hitRatio = hitCount / (hitCount + missCount);

            ps.println("Cache Region.........: " + cacheRegion);
            ps.println("Objects in Memory....: " + cacheStatistics.getElementCountInMemory());
            ps.println("Objects in Memory[MB]: " + (cacheStatistics.getSizeInMemory() / 1024D / 1024D));
            ps.println("Hit Count............: " + hitCount);
            ps.println("Miss Count...........: " + missCount);
            ps.println("Hit ratio[%].........: " + (hitRatio * 100));
            ps.println();
        }

        // Objektspezifische Statistiken
        @SuppressWarnings("deprecation")
        Metamodel metamodel = sessionFactory.getMetamodel();

        // Klassennamen sortieren
        // @formatter:off
        metamodel.getEntities().stream()
            .map(entityType -> entityType.getJavaType().getName())
            .sorted()
            .forEach(className -> {
            try
            {
                EntityStatistics entityStats = stats.getEntityStatistics(className);

                long inserts = entityStats.getInsertCount();
                long updates = entityStats.getUpdateCount();
                long deletes = entityStats.getDeleteCount();
                long fetches = entityStats.getFetchCount();
                long loads = entityStats.getLoadCount();
                long changes = inserts + updates + deletes;

                ps.println(className + " fetches " + fetches + " times");
                ps.println(className + " loads   " + loads + " times");
                ps.println(className + " inserts " + inserts + " times");
                ps.println(className + " updates " + updates + " times");
                ps.println(className + " deletes " + deletes + " times");
                ps.println(className + " changed " + changes + " times");
                ps.println();
            }
            catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }
        });
        // @formatter:on
    }

    /**
     *
     */
    public abstract void test1Insert();

    /**
     *
     */
    public abstract void test2SelectAll();

    /**
     *
     */
    public abstract void test3SelectVorname();

    /**
     *
     */
    public abstract void test4NativeQuery();

    /**
     * @param persons {@link List}
     */
    protected void validateTest1Insert(final List<Person> persons)
    {
        for (int i = 0; i < persons.size(); i++)
        {
            Person person = persons.get(i);
            Assert.assertEquals(10 + i, person.getID());
            Assert.assertEquals(3, person.getAddresses().size());
        }
    }

    /**
     * @param persons {@link List}
     */
    protected void validateTest2SelectAll(final List<Person> persons)
    {
        Assert.assertNotNull(persons);
        Assert.assertEquals(3, persons.size());

        for (Person person : persons)
        {
            LOGGER.info(person.toString());
            Assert.assertEquals(3, person.getAddresses().size());

            // for (Address address : person.getAddresses())
            // {
            // LOGGER.info("\t" + address.toString());
            // }
        }
    }

    /**
     * @param persons {@link List}
     * @param vorname String
     */
    protected void validateTest3SelectVorname(final List<Person> persons, final String vorname)
    {
        Assert.assertNotNull(persons);
        Assert.assertEquals(1, persons.size());

        Person person = persons.get(0);
        Assert.assertEquals(10, person.getID());
        Assert.assertEquals(vorname, person.getVorName());
        Assert.assertEquals(3, person.getAddresses().size());

        LOGGER.info(person.toString());

        // for (Address address : person.getAddresses())
        // {
        // LOGGER.info("\t" + address.toString);
        // }
    }
}
