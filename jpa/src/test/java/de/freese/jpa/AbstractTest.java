// Created: 22.11.2015
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import jakarta.persistence.SharedCacheMode;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.hibernate.SessionFactory;
import org.hibernate.cache.jcache.ConfigSettings;
import org.hibernate.cache.jcache.MissingCacheStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jpa.converter.StringStripConverter;
import de.freese.jpa.model.Address;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
abstract class AbstractTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger("TestLogger");

    @AfterAll
    static void afterAll() throws IOException {
        final Path ehCachePath = Paths.get(System.getProperty("java.io.tmpdir"), "ehcache");

        try (Stream<Path> stream = Files.walk(ehCachePath)) {
            stream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            //            stream.sorted(Comparator.reverseOrder()).forEach(Files::delete);
        }
    }

    // @Bean
    // public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(final javax.cache.CacheManager cacheManager) {
    //     return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    // }

    protected static Map<String, Object> getHibernateConfig() {
        final String id = UUID.randomUUID().toString();

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        hikariConfig.setJdbcUrl("jdbc:hsqldb:mem:" + id + ";shutdown=true");
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("");
        hikariConfig.setPoolName("hikari-" + hikariConfig.getJdbcUrl());
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(4);
        hikariConfig.setAutoCommit(false);

        final HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            final HikariPoolMXBean poolMXBean = hikariDataSource.getHikariPoolMXBean();

            LOGGER.info("Connections: idle={}, active={}, waiting={}", poolMXBean.getIdleConnections(), poolMXBean.getActiveConnections(),
                    poolMXBean.getThreadsAwaitingConnection());

            hikariDataSource.close();
        }, "shutdown"));

        // org.hibernate.cfg.Environment;
        final Map<String, Object> config = new HashMap<>();

        // Connection Properties
        // ****************************************************************************************
        // config.put(AvailableSettings.DATASOURCE, "jdbc/DS");
        config.put(AvailableSettings.JAKARTA_JTA_DATASOURCE, hikariDataSource);
        // config.put(AvailableSettings.DATASOURCE,hikariDataSource);
        // config.put(AvailableSettings.DIALECT, "org.hibernate.dialect.HSQLDialect"); // Auto detected
        // config.put(AvailableSettings.DRIVER, "org.hsqldb.jdbc.JDBCDriver");
        // config.put(AvailableSettings.URL, "jdbc:hsqldb:mem:" + System.currentTimeMillis() + ";shutdown=true");
        // config.put(AvailableSettings.URL, "jdbc:hsqldb:file:hsqldb/person;readonly=true;shutdown=true");
        // config.put(AvailableSettings.USER, "sa");
        // config.put(AvailableSettings.PASS, "");

        // Create Schema
        // ****************************************************************************************
        // config.put(AvailableSettings.HBM2DDL_AUTO, "none");
        config.put(AvailableSettings.HBM2DDL_AUTO, "create");
        // config.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        // config.put(AvailableSettings.HBM2DDL_IMPORT_FILES, "import.sql");

        // Logging
        // ****************************************************************************************
        // By the Property 'AvailableSettings.SHOW_SQL' Hibernate writes the Logs directly to the Console.
        // Better:
        // Logger 'org.hibernate.SQL' set to DEBUG.
        // Logger 'org.hibernate.orm.jdbc.bind' to TRACE for Parameter in Prepared-Statements.
        // Logger 'org.hibernate.orm.jdbc.extract' to TRACE for Values in Select-Statements.
        // config.put(AvailableSettings.SHOW_SQL, Boolean.toString(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()));
        config.put(AvailableSettings.FORMAT_SQL, Boolean.toString(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()));
        config.put(AvailableSettings.GENERATE_STATISTICS, Boolean.toString(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()));

        // Caching
        // ****************************************************************************************
        config.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
        // config.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.internal.NoCachingRegionFactory");
        config.put(AvailableSettings.CACHE_REGION_PREFIX, "hibernate.test");
        config.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        config.put(AvailableSettings.USE_QUERY_CACHE, "true");
        config.put(AvailableSettings.JAKARTA_SHARED_CACHE_MODE, SharedCacheMode.ALL);

        config.put(ConfigSettings.PROVIDER, "com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider");
        config.put(ConfigSettings.CONFIG_URI, "caffeine.conf");

        // config.put(ConfigSettings.PROVIDER, "org.ehcache.jsr107.EhcacheCachingProvider");
        // config.put(ConfigSettings.CONFIG_URI, "ehcache.xml");

        // Missing Caches are created automatically from the DEFAULT Cache-Configuration.
        config.put(ConfigSettings.MISSING_CACHE_STRATEGY, MissingCacheStrategy.CREATE_WARN.getExternalRepresentation());

        // config.put(ConfigSettings.CACHE_MANAGER, javax.cache.CacheManager);

        // Misc
        // ****************************************************************************************
        // config.put(AvailableSettings.DEFAULT_SCHEMA, "...");
        // config.put(AvailableSettings.SESSION_FACTORY_NAME, "de.freese.test"); // JNDI-Name
        config.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");
        config.put(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, "32");
        config.put(AvailableSettings.ISOLATION, "TRANSACTION_READ_COMMITTED");

        config.put(AvailableSettings.FLUSH_BEFORE_COMPLETION, "true");
        // config.put(AvailableSettings.JTA_PLATFORM, "<CLASS_NAME>");

        // config.put(AvailableSettings.QUERY_SUBSTITUTIONS, "true 1, false 0");
        config.put(AvailableSettings.ORDER_INSERTS, "true");
        config.put(AvailableSettings.ORDER_UPDATES, "true");
        config.put(AvailableSettings.STATEMENT_BATCH_SIZE, "30");
        config.put(AvailableSettings.STATEMENT_FETCH_SIZE, "100");

        // config.put(AvailableSettings.USE_STREAMS_FOR_BINARY, "true");
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
        final List<Person> persons = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            final Person person = Person.of("   Name" + i, "   Vorname" + i);
            persons.add(person);

            for (int j = 1; j <= 3; j++) {
                final Address address = Address.of("   Street" + i + j);
                person.addAddress(address);
            }
        }

        return persons;
    }

    protected void dumpStatistics(final PrintWriter pw, final SessionFactory sessionFactory) {
        Object jdbcUrl = null;

        for (String key : List.of(JdbcSettings.JAKARTA_JDBC_URL, JdbcSettings.JAKARTA_JTA_DATASOURCE, JdbcSettings.JAKARTA_NON_JTA_DATASOURCE)) {
            jdbcUrl = sessionFactory.getProperties().get(key);

            if (jdbcUrl != null) {
                break;
            }
        }

        pw.println("----------------------------------------------");
        pw.printf("PersistenceStatistics: %s%n", jdbcUrl);
        pw.println("----------------------------------------------");
        pw.println();

        final Statistics stats = sessionFactory.getStatistics();

        pw.println("Statistics enabled......: " + stats.isStatisticsEnabled());
        pw.println();

        pw.println("Start Date..............: " + stats.getStart());
        pw.println("Current Date............: " + LocalDateTime.now());
        pw.println();
        pw.println("PreparedStatement Count : " + stats.getPrepareStatementCount());
        pw.println("Session open Count......: " + stats.getSessionOpenCount());
        pw.println("Session close Count.....: " + stats.getSuccessfulTransactionCount());
        pw.println("Begin Transaction Count : " + stats.getTransactionCount());
        pw.println("Commit Transaction Count: " + stats.getSuccessfulTransactionCount());

        pw.println();
        pw.println("Query Cache");
        long hitCount = stats.getQueryCacheHitCount();
        long missCount = stats.getQueryCacheMissCount();
        double hitRatio = (double) hitCount / (double) (hitCount + missCount);

        if (Double.isNaN(hitRatio) || Double.isInfinite(hitRatio)) {
            hitRatio = 0D;
        }

        pw.println("SQL Query Hit Count..: " + hitCount);
        pw.println("SQL Query Miss Count.: " + missCount);
        pw.println("SQL Query Hit ratio %: " + BigDecimal.valueOf(hitRatio * 100D).setScale(3, RoundingMode.HALF_UP));

        pw.println();
        pw.println("2nd Level Cache");
        hitCount = stats.getSecondLevelCacheHitCount();
        missCount = stats.getSecondLevelCacheMissCount();
        hitRatio = (double) hitCount / (double) (hitCount + missCount);

        if (Double.isNaN(hitRatio) || Double.isInfinite(hitRatio)) {
            hitRatio = 0D;
        }

        pw.println("2nd Level Cache Hit Count...: " + hitCount);
        pw.println("2nd Level Cache Miss Count..: " + missCount);
        pw.println("2nd Level Cache Hit ratio[%]: " + BigDecimal.valueOf(hitRatio * 100D).setScale(3, RoundingMode.HALF_UP));

        pw.println();
        pw.println("2nd Level Cache-Regions");
        Stream.of(stats.getSecondLevelCacheRegionNames()).sorted().map(stats::getDomainDataRegionStatistics).filter(Objects::nonNull).forEach(cacheStatistics -> {
            final long hCount = cacheStatistics.getHitCount();
            final long mCount = cacheStatistics.getMissCount();
            double hRatio = (double) hCount / (double) (hCount + mCount);

            if (Double.isNaN(hRatio) || Double.isInfinite(hRatio)) {
                hRatio = 0D;
            }

            pw.println("Cache Region.........: " + cacheStatistics.getRegionName());
            pw.println("Objects in Memory....: " + cacheStatistics.getElementCountInMemory());
            pw.println("Objects in Memory[MB]: " + BigDecimal.valueOf(cacheStatistics.getSizeInMemory() / 1024D / 1024D).setScale(3, RoundingMode.HALF_UP));
            pw.println("Hit Count............: " + hCount);
            pw.println("Miss Count...........: " + mCount);
            pw.println("Hit ratio[%].........: " + BigDecimal.valueOf(hRatio * 100D).setScale(3, RoundingMode.HALF_UP));
            pw.println();
        });

        pw.println();
        pw.println("CollectionStatistics");
        Stream.of(stats.getCollectionRoleNames()).sorted().map(stats::getCollectionStatistics).filter(Objects::nonNull).forEach(collectionStatistics -> {
            final long hCount = collectionStatistics.getCacheHitCount();
            final long mCount = collectionStatistics.getCacheMissCount();
            double hRatio = (double) hCount / (double) (hCount + mCount);

            if (Double.isNaN(hRatio) || Double.isInfinite(hRatio)) {
                hRatio = 0D;
            }

            pw.println("Cache Region: " + collectionStatistics.getCacheRegionName());

            pw.println("Hit Count...: " + hCount);
            pw.println("Miss Count..: " + mCount);
            pw.println("Hit ratio[%]: " + BigDecimal.valueOf(hRatio * 100D).setScale(3, RoundingMode.HALF_UP));

            pw.println("Puts...: " + collectionStatistics.getCachePutCount());
            pw.println("Fetches: " + collectionStatistics.getFetchCount());
            pw.println("Loads..: " + collectionStatistics.getLoadCount());
            pw.println("Updates: " + collectionStatistics.getUpdateCount());
            pw.println();
        });

        pw.println();
        pw.println("QueryRegionStatistics");
        Stream.of(stats.getQueries()).sorted().map(stats::getQueryRegionStatistics).filter(Objects::nonNull).forEach(cacheRegionStatistics -> {
            final long hCount = cacheRegionStatistics.getHitCount();
            final long mCount = cacheRegionStatistics.getMissCount();
            double hRatio = (double) hCount / (double) (hCount + mCount);

            if (Double.isNaN(hRatio) || Double.isInfinite(hRatio)) {
                hRatio = 0D;
            }

            pw.println("Cache Region.........: " + cacheRegionStatistics.getRegionName());
            pw.println("Objects in Memory....: " + cacheRegionStatistics.getElementCountInMemory());
            pw.println("Objects in Memory[MB]: " + (cacheRegionStatistics.getSizeInMemory() / 1024D / 1024D));
            pw.println("Hit Count............: " + hCount);
            pw.println("Miss Count...........: " + mCount);
            pw.println("Hit ratio[%].........: " + BigDecimal.valueOf(hRatio * 100D).setScale(3, RoundingMode.HALF_UP));
            pw.println();
        });

        pw.println();
        pw.println("EntityStatistics");
        Stream.of(stats.getEntityNames()).sorted().map(stats::getEntityStatistics).filter(Objects::nonNull).forEach(entityStatistics -> {
            final long hCount = entityStatistics.getCacheHitCount();
            final long mCount = entityStatistics.getCacheMissCount();
            double hRatio = (double) hCount / (double) (hCount + mCount);

            if (Double.isNaN(hRatio) || Double.isInfinite(hRatio)) {
                hRatio = 0D;
            }

            pw.println("Cache Region: " + entityStatistics.getCacheRegionName());
            pw.println("Hit Count...: " + hCount);
            pw.println("Miss Count..: " + mCount);
            pw.println("Hit ratio[%]: " + BigDecimal.valueOf(hRatio * 100D).setScale(3, RoundingMode.HALF_UP));
            pw.println("Fetches: " + entityStatistics.getFetchCount());
            pw.println("Loads..: " + entityStatistics.getLoadCount());
            pw.println("Inserts: " + entityStatistics.getInsertCount());
            pw.println("Updates: " + entityStatistics.getUpdateCount());
            pw.println("Deletes: " + entityStatistics.getDeleteCount());
            pw.println();
        });

        // final Metamodel metamodel = sessionFactory.getMetamodel();
        // final Metamodel metamodel = ((SessionFactoryImplementor) sessionFactory).getMetamodel();
        // final Map<String, ClassMetadata> classMetadata = sessionFactory.getAllClassMetadata();
        //
        // Sort by Class name.
        // classMetadata.values().stream()
        //     .map(cmd -> cmd.getEntityName())

        // metamodel.getEntities().stream()
        //     .map(entityType -> entityType.getJavaType().getName())
        //     .sorted()
        //     .forEach(className -> {

        //        final Cache cache = sessionFactory.getCache();
        //        final CacheImplementor cacheImplementor = (CacheImplementor) cache;
        //        final RegionFactory regionFactory = cacheImplementor.getRegionFactory();
        //        final JCacheRegionFactory jCacheRegionFactory = (JCacheRegionFactory) regionFactory;
        //        final CacheManager cacheManager = jCacheRegionFactory.getCacheManager();

        pw.println();
        pw.flush();
    }

    protected void validateTest1Insert(final List<Person> persons) {
        for (int i = 0; i < persons.size(); i++) {
            final Person person = persons.get(i);

            assertEquals(1 + i, person.getID());
            assertEquals(3, person.getAddresses().size());

            for (int j = 0; j < person.getAddresses().size(); j++) {
                final Address address = person.getAddresses().get(j);

                final long addressIdExpected = ((person.getID() - 1) * person.getAddresses().size()) + j + 1;
                assertEquals(addressIdExpected, address.getID());
                assertEquals(person, address.getPerson());
            }
        }
    }

    protected void validateTest2SelectAll(final List<Person> persons) {
        assertNotNull(persons);
        assertEquals(3, persons.size());

        for (int i = 0; i < persons.size(); i++) {
            final Person person = persons.get(i);
            LOGGER.info(person.toString());

            assertEquals(1 + i, person.getID());
            assertEquals(3, person.getAddresses().size());

            // for (Address address : person.getAddresses()) {
            // LOGGER.info("\t" + address.toString());
            // }
        }
    }

    protected void validateTest3SelectVorname(final List<Person> persons, final String vorname) {
        assertNotNull(persons);
        assertEquals(1, persons.size());

        final Person person = persons.get(0);
        assertEquals(1, person.getID());
        assertEquals(vorname, person.getVorname());
        assertEquals(3, person.getAddresses().size());

        LOGGER.info(person.toString());

        // for (Address address : person.getAddresses()) {
        // LOGGER.info("\t" + address.toString);
        // }
    }
}
