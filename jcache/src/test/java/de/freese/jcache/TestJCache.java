// Created: 06 Juli 2024
package de.freese.jcache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.hazelcast.config.AwsConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.PartitionGroupConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jcache.impl.CaffeineJCache;
import de.freese.jcache.impl.HazelcastJCache;
import de.freese.jcache.impl.MapJCache;
import de.freese.jcache.spi.DefaultCacheManager;
import de.freese.jcache.spi.SimpleCachingProvider;

/**
 * @author Thomas Freese
 */
class TestJCache {
    private static CachingProvider cachingProvider;
    private static HazelcastInstance hazelcastInstance;

    @AfterAll
    static void afterAll() {
        cachingProvider.close();

        hazelcastInstance.shutdown();
        Hazelcast.shutdownAll();
    }

    @BeforeAll
    static void beforeAll() {
        // If multiple CachingProvider are present.
        System.setProperty(Caching.JAVAX_CACHE_CACHING_PROVIDER, SimpleCachingProvider.class.getName());
        cachingProvider = Caching.getCachingProvider();

        // cachingProvider = Caching.getCachingProvider(SimpleCachingProvider.class.getName());

        ((SimpleCachingProvider) cachingProvider).setCacheFactory((cacheManager, cacheName) -> {
            switch (cacheName) {
                case "map" -> {
                    return new MapJCache<>(cacheManager, cacheName, new TreeMap<>());
                }

                case "caffeine" -> {
                    // final Caffeine<Object, Object> caffeine = Caffeine.from("maximumSize=10,expireAfterAccess=3s,recordStats");
                    final Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                            .maximumSize(100)
                            .expireAfterAccess(Duration.ofHours(12))
                            .recordStats();

                    final Logger logger = LoggerFactory.getLogger(TestJCache.class);
                    final com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache = caffeine
                            .evictionListener((key, value, cause) -> logger.info("Eviction: {} - {} = {}", cause, key, value))
                            .removalListener((key, value, cause) -> logger.info("Removal: {} - {} = {}", cause, key, value))
                            .build();

                    return new CaffeineJCache<>(cacheManager, cacheName, caffeineCache);
                }

                case "hazelcast" -> {
                    if (hazelcastInstance == null) {
                        final MapConfig mapConfig = new MapConfig("default")
                                .setEvictionConfig(new EvictionConfig()
                                        .setSize(3)
                                        .setEvictionPolicy(EvictionPolicy.LRU)
                                        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                                );

                        final Config config = new Config()
                                .addMapConfig(mapConfig)
                                .setClusterName("my-test")
                                .setInstanceName("my-test-instance")
                                .setPartitionGroupConfig(new PartitionGroupConfig().setEnabled(false))
                                .setJetConfig(new JetConfig().setEnabled(true))
                                .setNetworkConfig(new NetworkConfig()
                                        .setInterfaces(new InterfacesConfig().setEnabled(false))
                                        .setJoin(new JoinConfig()
                                                .setMulticastConfig(new MulticastConfig().setEnabled(false))
                                                .setTcpIpConfig(new TcpIpConfig().setEnabled(false))
                                                .setAwsConfig(new AwsConfig().setEnabled(false))
                                        )
                                )
                                .setProperty("hazelcast.logging.type", "slf4j")
                                .setProperty("hazelcast.partition.count", "1");

                        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
                    }

                    return new HazelcastJCache<>(cacheManager, cacheName, hazelcastInstance.getMap(cacheName));
                }
                
                default -> throw new UnsupportedOperationException("unsupported cache");
            }
        });
    }

    static Stream<Arguments> getCacheNames() {
        return Stream.of(
                Arguments.of("map"),
                Arguments.of("caffeine"),
                Arguments.of("hazelcast")
        );
    }

    @AfterEach
    void afterEach() {
        // Empty
    }

    @BeforeEach
    void beforeEach() {
        // Empty
    }

    // CachingProvider cachingProvider = Caching.getCachingProvider(SimpleCachingProvider.class.getName())

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("getCacheNames")
    @DisplayName("Test JCache Adapter")
    void testJCache(final String cacheName) {
        assertInstanceOf(SimpleCachingProvider.class, cachingProvider);

        assertNotNull(Caching.getCache(cacheName, String.class, String.class));

        try (CacheManager cacheManager = cachingProvider.getCacheManager()) {
            assertNotNull(cacheManager);
            assertInstanceOf(DefaultCacheManager.class, cacheManager);

            try (Cache<String, String> cache = cacheManager.getCache(cacheName)) {
                assertNotNull(cache);

                assertUnwrap(cacheName, cache);

                assertNull(cache.get("key"));
                assertFalse(cache.containsKey("key"));

                cache.put("key", "value");
                assertNotNull(cache.get("key"));
                assertTrue(cache.containsKey("key"));
                assertEquals("value", cache.get("key"));

                final List<Cache.Entry<String, String>> entries = new ArrayList<>();
                cache.iterator().forEachRemaining(entries::add);
                assertEquals(1, entries.size());
                assertEquals("key", entries.getFirst().getKey());
                assertEquals("value", entries.getFirst().getValue());

                assertFalse(cacheManager.getCache(cacheName).isClosed());
            }

            assertTrue(cacheManager.getCache(cacheName).isClosed());

            try (Cache<String, String> cache = cacheManager.createCache(cacheName, null)) {
                assertNotNull(cache);

                assertUnwrap(cacheName, cache);

                // Already closed
                assertTrue(cacheManager.getCache(cacheName).isClosed());

                assertThrows(IllegalStateException.class, () -> cache.get("key"), "Cache is closed: " + cacheName);
            }

            assertTrue(cacheManager.getCache(cacheName).isClosed());
        }
    }

    private void assertUnwrap(final String cacheName, final Cache<?, ?> cache) {
        if ("caffeine".equalsIgnoreCase(cacheName)) {
            assertInstanceOf(CaffeineJCache.class, cache.unwrap(CaffeineJCache.class));
            assertInstanceOf(com.github.benmanes.caffeine.cache.Cache.class, cache.unwrap(com.github.benmanes.caffeine.cache.Cache.class));
        }
        else if ("map".equalsIgnoreCase(cacheName)) {
            assertInstanceOf(MapJCache.class, cache.unwrap(MapJCache.class));
            assertInstanceOf(Map.class, cache.unwrap(Map.class));
        }
        else if ("hazelcast".equalsIgnoreCase(cacheName)) {
            assertInstanceOf(HazelcastJCache.class, cache.unwrap(HazelcastJCache.class));
            assertInstanceOf(IMap.class, cache.unwrap(IMap.class));
        }
        else {
            throw new UnsupportedOperationException("unsupported cache");
        }
    }
}
