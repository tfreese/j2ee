// Created: 22.11.2015
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import javax.cache.CacheManager;

import jakarta.persistence.SharedCacheMode;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.Cache;
import org.hibernate.SessionFactory;
import org.hibernate.cache.jcache.ConfigSettings;
import org.hibernate.cache.jcache.MissingCacheStrategy;
import org.hibernate.cache.jcache.internal.JCacheRegionFactory;
import org.hibernate.cache.spi.CacheImplementor;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jpa.model.Address;
import de.freese.jpa.model.Person;
import de.freese.jpa.utils.StringStripConverter;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
abstract class AbstractTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger("TestLogger");

    @AfterAll
    static void afterAll() throws IOException {
        Path ehCachePath = Paths.get(System.getProperty("java.io.tmpdir"), "ehcache");

        try (Stream<Path> stream = Files.walk(ehCachePath)) {
            stream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            //            stream.sorted(Comparator.reverseOrder()).forEach(Files::delete);
        }
    }

    protected static Map<String, Object> getHibernateConfig() {
        //        long id = System.nanoTime();
        String id = UUID.randomUUID().toString();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        hikariConfig.setJdbcUrl("jdbc:hsqldb:mem:" + id + ";shutdown=true");
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("");
        hikariConfig.setPoolName("hikari-" + id);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(8);
        hikariConfig.setAutoCommit(false);

        // org.hibernate.cfg.Environment;
        Map<String, Object> config = new HashMap<>();

        // Connection Properties
        // ****************************************************************************************
        // config.put(AvailableSettings.DATASOURCE, "jdbc/DS");
        //        config.put(AvailableSettings.DATASOURCE, new HikariDataSource(hikariConfig));
        config.put("hibernate.connection.datasource", new HikariDataSource(hikariConfig));
        config.put(AvailableSettings.DIALECT, "org.hibernate.dialect.HSQLDialect");
        //        config.put(AvailableSettings.DRIVER, "org.hsqldb.jdbc.JDBCDriver");
        //        config.put(AvailableSettings.URL, "jdbc:hsqldb:mem:" + System.currentTimeMillis());
        // config.put(AvailableSettings.URL, "jdbc:hsqldb:file:hsqldb/person;readonly=true;shutdown=true");
        //        config.put(AvailableSettings.USER, "sa");
        //        config.put(AvailableSettings.PASS, "");

        // Create Schema
        // ****************************************************************************************
        // config.put(AvailableSettings.HBM2DDL_AUTO, "none");
        config.put(AvailableSettings.HBM2DDL_AUTO, "create");
        // config.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        // config.put(AvailableSettings.HBM2DDL_IMPORT_FILES, "import.sql");

        // Logging
        // ****************************************************************************************
        // By the Property 'AvailableSettings.SHOW_SQL' Hibernate writes the Logs to the Console.
        // Better: Logger 'org.hibernate.SQL' set to DEBUG.
        // Logger 'org.hibernate.orm.jdbc.bind' to TRACE for Parameter in Prepared-Statements.
        // Logger 'org.hibernate.orm.jdbc.extract' to TRACE for Werte in Select-Statements.
        // config.put(AvailableSettings.SHOW_SQL, Boolean.toString(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()));
        config.put(AvailableSettings.FORMAT_SQL, Boolean.toString(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()));
        config.put(AvailableSettings.GENERATE_STATISTICS, Boolean.toString(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()));

        // Caching
        // ****************************************************************************************
        // config.put(AvailableSettings.CACHE_REGION_FACTORY, "de.freese.jpa.OnTheFlyEhcacheRegionFactory");
        config.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
        //        config.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.internal.NoCachingRegionFactory");
        config.put(AvailableSettings.CACHE_REGION_PREFIX, "hibernate.test." + id);
        config.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        config.put(AvailableSettings.USE_QUERY_CACHE, "true");
        config.put(AvailableSettings.JAKARTA_SHARED_CACHE_MODE, SharedCacheMode.ALL);

        config.put(ConfigSettings.CONFIG_URI, "ehcache.xml");

        // Missing Caches are created automatically from the DEFAULT Cache-Configuration.
        config.put(ConfigSettings.MISSING_CACHE_STRATEGY, MissingCacheStrategy.CREATE_WARN.getExternalRepresentation());

        // Misc
        // ****************************************************************************************
        // config.put(AvailableSettings.DEFAULT_SCHEMA, "...");
        // config.put(AvailableSettings.SESSION_FACTORY_NAME, "de.freese.test"); // JNDI-Name
        config.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");
        config.put(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, "32");
        config.put(AvailableSettings.ISOLATION, "TRANSACTION_READ_COMMITTED");

        config.put(AvailableSettings.FLUSH_BEFORE_COMPLETION, "true");
        // config.put(AvailableSettings.JTA_PLATFORM, "<CLASS_NAME>");

        //        config.put(AvailableSettings.QUERY_SUBSTITUTIONS, "true 1, false 0");
        config.put(AvailableSettings.ORDER_INSERTS, "true");
        config.put(AvailableSettings.ORDER_UPDATES, "true");
        config.put(AvailableSettings.STATEMENT_BATCH_SIZE, "30");
        config.put(AvailableSettings.STATEMENT_FETCH_SIZE, "100");

        //        config.put(AvailableSettings.USE_STREAMS_FOR_BINARY, "true");
        config.put(AvailableSettings.USE_MINIMAL_PUTS, "false");
        config.put(AvailableSettings.USE_STRUCTURED_CACHE, "false");

        // config.put(ENTITY_INTERCEPTOR_CLASS, "... extends org.hibernate.EmptyInterceptor");
        return config;
    }

    public abstract void test010Insert();

    public abstract void test020SelectAll();

    public abstract void test030SelectVorname();

    public abstract void test040NativeQuery();

    /**
     * Spaces will be removed by {@link StringStripConverter}.
     **/
    protected List<Person> createPersons() {
        List<Person> persons = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            Person person = new Person("   Name" + i, "   Vorname" + i);
            persons.add(person);

            for (int j = 1; j <= 3; j++) {
                Address address = new Address("   Street" + i + j);
                person.addAddress(address);
            }
        }

        return persons;
    }

    protected void dumpStatistics(final PrintStream ps, final SessionFactory sessionFactory) {
        Statistics stats = sessionFactory.getStatistics();

        long txCount = stats.getTransactionCount();
        long successfulTxCount = stats.getSuccessfulTransactionCount();

        ps.println("PreparedStatement Count : " + stats.getPrepareStatementCount());
        ps.println("Session open Count......: " + stats.getSessionOpenCount());
        ps.println("Session close Count.....: " + successfulTxCount);
        ps.println("Begin Transaction Count : " + txCount);
        ps.println("Commit Transaction Count: " + stats.getSuccessfulTransactionCount());

        // Global 2nd lvl Cache
        double hitRatio = 0;

        if ((stats.getSecondLevelCacheHitCount() + stats.getSecondLevelCacheMissCount()) > 0) {
            hitRatio = stats.getSecondLevelCacheHitCount() / (stats.getSecondLevelCacheHitCount() + stats.getSecondLevelCacheMissCount());
        }

        ps.println("Second Cache Hit Count...: " + stats.getSecondLevelCacheHitCount());
        ps.println("Second Cache Miss Count..: " + stats.getSecondLevelCacheMissCount());
        ps.println("Second Cache Hit ratio[%]: " + (hitRatio * 100));

        // Globaler Query Cache
        hitRatio = 0;

        if ((stats.getQueryCacheHitCount() + stats.getQueryCacheMissCount()) > 0) {
            hitRatio = stats.getQueryCacheHitCount() / (stats.getQueryCacheHitCount() + stats.getQueryCacheMissCount());
        }

        ps.println("SQL Query Hit Count: " + stats.getQueryCacheHitCount());
        ps.println("SQL Query Miss Count: " + stats.getQueryCacheMissCount());
        ps.println("SQL Query Hit ratio %: " + (hitRatio * 100));

        ps.println();
        ps.println("CollectionStatistics");
        Stream.of(stats.getCollectionRoleNames()).sorted().map(stats::getCollectionStatistics).filter(s -> s != null).forEach(s -> {
            long hitCount = s.getCacheHitCount();
            long missCount = s.getCacheMissCount();
            double ratio = hitCount / (hitCount + missCount);

            ps.println("Cache Region.........: " + s);

            ps.println("Hit Count............: " + hitCount);
            ps.println("Miss Count...........: " + missCount);
            ps.println("Hit ratio[%].........: " + (ratio * 100));

            ps.println(s.getCacheRegionName() + " puts " + s.getCachePutCount());
            ps.println(s.getCacheRegionName() + " fetches " + s.getFetchCount());
            ps.println(s.getCacheRegionName() + " loads " + s.getLoadCount());
            ps.println(s.getCacheRegionName() + " updates " + s.getUpdateCount());
            ps.println();
        });

        ps.println();
        ps.println("QueryRegionStatistics");
        Stream.of(stats.getQueries()).sorted().map(stats::getQueryRegionStatistics).filter(Objects::nonNull).forEach(s -> {
            long hitCount = s.getHitCount();
            long missCount = s.getMissCount();
            double ratio = hitCount / (hitCount + missCount);

            ps.println("Cache Region.........: " + s);
            ps.println("Objects in Memory....: " + s.getElementCountInMemory());
            ps.println("Objects in Memory[MB]: " + (s.getSizeInMemory() / 1024D / 1024D));
            ps.println("Hit Count............: " + hitCount);
            ps.println("Miss Count...........: " + missCount);
            ps.println("Hit ratio[%].........: " + (ratio * 100));
            ps.println();
        });

        // Objekt specific Statistics
        // Metamodel metamodel = sessionFactory.getMetamodel();
        // Metamodel metamodel = ((SessionFactoryImplementor) sessionFactory).getMetamodel();
        // Map<String, ClassMetadata> classMetadata = sessionFactory.getAllClassMetadata();

        // Sort by Class name.
        // @formatter:off
//        classMetadata.values().stream()
//            .map(cmd -> cmd.getEntityName())

//        metamodel.getEntities().stream()
//            .map(entityType -> entityType.getJavaType().getName())
//            .sorted()
//            .forEach(className -> {
        ps.println();
        ps.println("EntityStatistics");
        Stream.of(stats.getEntityNames()).sorted().map(stats::getEntityStatistics).filter(Objects::nonNull).forEach(ps::println);
        // @formatter:on

        ps.println();

        Cache cache = sessionFactory.getCache();
        CacheImplementor cacheImplementor = (CacheImplementor) cache;
        RegionFactory regionFactory = cacheImplementor.getRegionFactory();
        JCacheRegionFactory jCacheRegionFactory = (JCacheRegionFactory) regionFactory;
        CacheManager cacheManager = jCacheRegionFactory.getCacheManager();

        ps.println();
    }

    protected void validateTest1Insert(final List<Person> persons) {
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);

            assertEquals(1 + i, person.getID());
            assertEquals(3, person.getAddresses().size());

            for (int j = 0; j < person.getAddresses().size(); j++) {
                Address address = person.getAddresses().get(j);

                long addressIdExpected = ((person.getID() - 1) * person.getAddresses().size()) + j + 1;
                assertEquals(addressIdExpected, address.getID());
                assertEquals(person, address.getPerson());
            }
        }
    }

    protected void validateTest2SelectAll(final List<Person> persons) {
        assertNotNull(persons);
        assertEquals(3, persons.size());

        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            LOGGER.info(person.toString());

            assertEquals(1 + i, person.getID());
            assertEquals(3, person.getAddresses().size());

            // for (Address address : person.getAddresses())
            // {
            // LOGGER.info("\t" + address.toString());
            // }
        }
    }

    protected void validateTest3SelectVorname(final List<Person> persons, final String vorname) {
        assertNotNull(persons);
        assertEquals(1, persons.size());

        Person person = persons.get(0);
        assertEquals(1, person.getID());
        assertEquals(vorname, person.getVorname());
        assertEquals(3, person.getAddresses().size());

        LOGGER.info(person.toString());

        // for (Address address : person.getAddresses())
        // {
        // LOGGER.info("\t" + address.toString);
        // }
    }
}
