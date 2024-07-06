// Created: 22.11.2015
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.cache.Cache;
import javax.cache.CacheManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.SharedCacheMode;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.SchemaAutoTooling;
import org.hibernate.cache.jcache.ConfigSettings;
import org.hibernate.cache.jcache.MissingCacheStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jpa.cache.JCacheCaffeine;
import de.freese.jpa.cache.JCacheManager;
import de.freese.jpa.model.Address;
import de.freese.jpa.model.MyProjectionVo;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
abstract class AbstractTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger("TestLogger");

    @AfterAll
    static void cleanUp() throws IOException {
        final Path ehCachePath = Paths.get(System.getProperty("java.io.tmpdir"), "ehcache");

        if (!Files.exists(ehCachePath)) {
            return;
        }

        try (Stream<Path> stream = Files.walk(ehCachePath)) {
            stream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            //            stream.sorted(Comparator.reverseOrder()).forEach(Files::delete);
        }
    }

    // @Bean
    // public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(final javax.cache.CacheManager cacheManager) {
    //     return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    // }

    protected static Map<String, Object> getHibernateConfig() throws Throwable {
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
        // config.put(AvailableSettings.JAKARTA_JTA_DATASOURCE, "jdbc/DS");
        config.put(AvailableSettings.JAKARTA_JTA_DATASOURCE, hikariDataSource);
        // config.put(AvailableSettings.DIALECT, "org.hibernate.dialect.HSQLDialect"); // Auto detected
        // config.put(AvailableSettings.JAKARTA_JDBC_DRIVER, "org.hsqldb.jdbc.JDBCDriver");
        // config.put(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:hsqldb:mem:" + System.currentTimeMillis() + ";shutdown=true");
        // config.put(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:hsqldb:file:hsqldb/person;readonly=true;shutdown=true");
        // config.put(AvailableSettings.JAKARTA_JDBC_USER, "sa");
        // config.put(AvailableSettings.JAKARTA_JDBC_PASSWORD, "");

        // Schema
        // ****************************************************************************************
        // config.put(AvailableSettings.HBM2DDL_AUTO, "none");
        config.put(AvailableSettings.HBM2DDL_AUTO, SchemaAutoTooling.UPDATE.name().toLowerCase());
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
        config.put(AvailableSettings.CACHE_REGION_PREFIX, "hibernate.test");
        config.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        config.put(AvailableSettings.USE_QUERY_CACHE, "true");
        config.put(AvailableSettings.USE_MINIMAL_PUTS, "false");
        config.put(AvailableSettings.USE_STRUCTURED_CACHE, "false");
        config.put(AvailableSettings.JAKARTA_SHARED_CACHE_MODE, SharedCacheMode.ALL);

        // config.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.internal.NoCachingRegionFactory");
        config.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.jcache.internal.JCacheRegionFactory");

        // Missing Caches are created automatically from the DEFAULT Cache-Configuration.
        config.put(ConfigSettings.MISSING_CACHE_STRATEGY, MissingCacheStrategy.CREATE_WARN.getExternalRepresentation());

        // config.put(ConfigSettings.PROVIDER, "com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider");
        // config.put(ConfigSettings.CONFIG_URI, "caffeine.conf");

        // config.put(ConfigSettings.PROVIDER, "org.ehcache.jsr107.EhcacheCachingProvider");
        // config.put(ConfigSettings.CONFIG_URI, "ehcache.xml");

        final BiFunction<CacheManager, String, Cache<?, ?>> cacheFactory = (cacheManager, cacheName) -> {
            final Caffeine<Object, Object> caffeine;

            if ("person".equals(cacheName)) {
                caffeine = Caffeine.from("maximumSize=10,expireAfterAccess=3s,recordStats");
            }
            else {
                caffeine = Caffeine.from("maximumSize=1000,expireAfterAccess=12h");
            }

            final com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache = caffeine
                    .evictionListener((key, value, cause) -> LOGGER.info("Eviction: {} - {} = {}", cause, key, value))
                    .removalListener((key, value, cause) -> LOGGER.info("Removal: {} - {} = {}", cause, key, value))
                    .build();

            return new JCacheCaffeine<>(cacheManager, cacheName, caffeineCache);
        };
        config.put(ConfigSettings.CACHE_MANAGER, new JCacheManager(true, cacheFactory));

        // config.put(ConfigSettings.PROVIDER, "de.freese.jpa.cache.JCachingProvider");
        //
        // final Class<?> clazz = Class.forName("de.freese.jpa.cache.JCachingProvider");
        // Method method = clazz.getDeclaredMethod("setCreateLazy", boolean.class);
        // method.invoke(null, true);
        // method = clazz.getDeclaredMethod("setCacheFactory", BiFunction.class);
        // method.invoke(null, cacheFactory);
        //
        // final MethodHandles.Lookup methodLookup = MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());
        // MethodHandle handle = methodLookup.findStaticSetter(clazz, "createLazy", boolean.class);
        // handle.invoke(true);
        // handle = methodLookup.findStaticSetter(clazz, "cacheFactory", BiFunction.class);
        // handle.invoke(cacheFactory);

        // Misc
        // ****************************************************************************************
        // config.put(AvailableSettings.DEFAULT_SCHEMA, "...");
        // config.put(AvailableSettings.SESSION_FACTORY_NAME, "de.freese.test"); // JNDI-Name
        config.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");
        config.put(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, "32");
        config.put(AvailableSettings.ISOLATION, String.valueOf(Connection.TRANSACTION_READ_COMMITTED));

        config.put(AvailableSettings.FLUSH_BEFORE_COMPLETION, "true");
        // config.put(AvailableSettings.JTA_PLATFORM, "<CLASS_NAME>");

        // config.put(AvailableSettings.QUERY_SUBSTITUTIONS, "true 1, false 0");
        config.put(AvailableSettings.ORDER_INSERTS, "true");
        config.put(AvailableSettings.ORDER_UPDATES, "true");
        config.put(AvailableSettings.STATEMENT_BATCH_SIZE, "30");
        config.put(AvailableSettings.STATEMENT_FETCH_SIZE, "100");

        // config.put(AvailableSettings.USE_STREAMS_FOR_BINARY, "true");

        // config.put(ENTITY_INTERCEPTOR_CLASS, "... extends org.hibernate.EmptyInterceptor");

        return config;
    }

    @Test
    void test000Unwrap() {
        assertInstanceOf(SessionFactory.class, getEntityManagerFactory().unwrap(SessionFactory.class));

        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            assertInstanceOf(Session.class, entityManager.unwrap(Session.class));
        }
    }

    @Test
    void test010Insert() {
        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            entityManager.getTransaction().begin();

            final List<Person> persons = new ArrayList<>();

            // Spaces will be removed by {@link StringStripConverter}.
            for (int i = 1; i <= 3; i++) {
                final Person person = Person.of("   Name" + i, "   Vorname" + i);
                persons.add(person);

                for (int j = 1; j <= 3; j++) {
                    final Address address = Address.of("   Street" + i + j);
                    person.addAddress(address);
                }

                entityManager.persist(person);
            }

            validatePersons(persons);

            // entityManager.flush(); // without no flush -> no insert
            entityManager.getTransaction().commit();
        }
    }

    @Test
    void test020SelectAll() {
        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            // Caching must be enabled explicitly.
            // final List<Person> persons = entityManager.createQuery("from Person order by id asc")
            // .setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person").getResultList();
            // session.createQuery("from Person order by id asc").setCacheable(true).setCacheRegion("person").getResultList();

            // Caching is enabled in Mapping.
            final List<Person> persons = entityManager.createNamedQuery("allPersons", Person.class).getResultList();

            // validateTest2SelectAll(persons);
            validatePersons(persons);
        }
    }

    @Test
    void test030SelectVorname() {
        final String vorname = "Vorname1";

        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            // Caching is enabled in Mapping.
            final Person person = entityManager.createNamedQuery("personByVorname", Person.class).setParameter("vorname", vorname).getSingleResult();

            // Caching must be enabled explicitly.
            // final Person person = entityManager.createQuery("from Person where vorname=:vorname order by name asc", Person.class)
            // setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person").getSingleResult();
            // session.createQuery("from Person where vorname=:vorname order by name asc").setCacheable(true).setCacheRegion("person").getSingleResult();

            assertNotNull(person);
            assertEquals(1, person.getID());
            assertEquals(vorname, person.getVorname());
            assertEquals(3, person.getAddresses().size());

            LOGGER.info(person.toString());
        }
    }

    @Test
    void test040NativeQuery() {
        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            // !!! Aliases won't work in Native-Queries without Mapping object !!!
            // !!! Scalar Values (addScalar) like in Hibernate are not working for JPA !!!
            // !!! No Caching for Named-Queries !!!
            final List<Person> persons = entityManager.createNamedQuery("allPersons.native", Object[].class)
                    //            .setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "person")
                    .getResultStream().map(row -> {
                        final Person person = Person.of((String) row[1], (String) row[2]);
                        person.setID((long) row[0]);
                        return person;
                    }).toList();

            // Force flush on T_PERSON, so the NativeQuery can access the cached Data from the Session.
            // final List<Person> persons = query.unwrap(NativeQuery.class).addSynchronizedQuerySpace("T_PERSON").getResultList();

            assertNotNull(persons);
            assertFalse(persons.isEmpty());

            final Query queryAddress = entityManager.createNativeQuery("select id, street from T_ADDRESS where person_id = :personId order by street desc", Object[].class);
            // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE).setHint(QueryHints.CACHE_REGION, "address");

            for (Person person : persons) {
                // query.setParameter("personId", person.getID()).getResultStream().map(Object[].class::cast).map(row -> {
                //     final Address address = new Address((String) row[1]);
                //     address.setID((long) row[0]);
                //     return address;
                // }).forEach(person::addAddress);

                @SuppressWarnings("unchecked") final List<Object[]> addresses = queryAddress.setParameter("personId", person.getID()).getResultList();

                assertNotNull(addresses);
                assertFalse(addresses.isEmpty());

                addresses.forEach(row -> {
                    final Address address = Address.of((String) row[1]);
                    address.setID((long) row[0]);

                    person.addAddress(address);
                });
            }

            validatePersons(persons);
        }
    }

    @Test
    void test050Projection() {
        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            final String hql = """
                    select
                        new de.freese.jpa.model.MyProjectionVo(
                        p.id,
                        p.name
                        )
                    from
                        Person p
                    order by p.name asc
                    """;

            final List<MyProjectionVo> result = entityManager.createQuery(hql, MyProjectionVo.class).getResultList();

            assertNotNull(result);
            assertFalse(result.isEmpty());

            for (int i = 1; i <= result.size(); i++) {
                final MyProjectionVo dto = result.get(i - 1);

                assertEquals("Name" + i, dto.getName());
            }
        }
    }

    @Test
    void test060Update() {
        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            entityManager.getTransaction().begin();

            // With additional Select.
            final Person person = entityManager.find(Person.class, 1);
            assertNotNull(person);
            person.setName("newName");
            entityManager.persist(person);

            // Does not update Timestamps (@UpdateTimestamp).
            // final int affectedRows = entityManager.createQuery("update Person p set p.name = :name where p.id = :id")
            //         .setParameter("name", "newName")
            //         .setParameter("id", 1)
            //         .executeUpdate();
            // assertEquals(1, affectedRows);

            entityManager.getTransaction().commit();

            assertTrue(person.getUpdated().isAfter(person.getCreated()));
        }
    }

    @Test
    void test070Delete() {
        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            entityManager.getTransaction().begin();

            // Plain delete.
            final Person person = entityManager.getReference(Person.class, 1);
            assertNotNull(person);
            entityManager.remove(person);

            // Alternative with additional Select.
            // final Person person = entityManager.find(Person.class, 1);
            // assertNotNull(person);
            // entityManager.remove(person);

            // Plain delete, doesn't delete Associations.
            // final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            // final CriteriaDelete<Person> delete = builder.createCriteriaDelete(Person.class);
            // final Root<Person> root = delete.from(Person.class);
            // delete.where(builder.equal(root.get("id"), 1));
            // final int affectedRows = entityManager.createQuery(delete).executeUpdate();
            // assertEquals(1, affectedRows);

            // Plain delete, doesn't delete Associations.
            // final int affectedRows = entityManager.createQuery("delete Person p where p.id = :id")
            //         .setParameter("id", 1)
            //         .executeUpdate();
            // assertEquals(1, affectedRows);

            entityManager.getTransaction().commit();

            final long count = entityManager.createQuery("select count(*) from Person", long.class).getSingleResult();
            assertEquals(2, count);
        }
    }

    @Test
    void test099Statistics() {
        dumpStatistics(new PrintWriter(System.out), getEntityManagerFactory().unwrap(SessionFactory.class));

        assertTrue(true);
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
        Stream.of(stats.getSecondLevelCacheRegionNames()).sorted().map(stats::getCacheRegionStatistics).filter(Objects::nonNull).forEach(cacheStatistics -> {
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

    protected abstract EntityManagerFactory getEntityManagerFactory();

    protected void validatePersons(final List<Person> origin) {
        final List<Person> persons = new ArrayList<>(origin);
        persons.sort(Comparator.comparing(Person::getID));

        for (int i = 0; i < persons.size(); i++) {
            final Person person = persons.get(i);

            assertEquals(1 + i, person.getID());
            assertEquals(3, person.getAddresses().size());

            final List<Address> addresses = new ArrayList<>(person.getAddresses());
            addresses.sort(Comparator.comparing(Address::getID));

            for (int j = 0; j < addresses.size(); j++) {
                final Address address = addresses.get(j);

                final long addressIdExpected = ((person.getID() - 1) * addresses.size()) + j + 1;
                assertEquals(addressIdExpected, address.getID());
                assertEquals(person, address.getPerson());
            }
        }
    }
}
