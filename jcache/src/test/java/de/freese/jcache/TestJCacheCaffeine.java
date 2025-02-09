// Created: 06 Juli 2024
package de.freese.jcache;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.BiFunction;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jcache.spi.DefaultCacheManager;
import de.freese.jcache.spi.SimpleCachingProvider;

/**
 * @author Thomas Freese
 */
class TestJCacheCaffeine {

    // CachingProvider cachingProvider = Caching.getCachingProvider(SimpleCachingProvider.class.getName())

    @Test
    void testCaffeine() {
        final BiFunction<CacheManager, String, Cache<Object, Object>> cacheFactory = (cacheManager, name) -> {
            final Caffeine<Object, Object> caffeine;

            if ("a".equals(name)) {
                caffeine = Caffeine.from("maximumSize=10,expireAfterAccess=3s,recordStats");
            }
            else {
                caffeine = Caffeine.from("maximumSize=1000,expireAfterAccess=12h");
            }

            final Logger logger = LoggerFactory.getLogger(getClass());
            final com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache = caffeine
                    // .executor(Executors.newFixedThreadPool(3, new NamedThreadFactory("caffeine-executor-%d", true)))
                    .evictionListener((key, value, cause) -> logger.info("Eviction: {} - {} = {}", cause, key, value))
                    .removalListener((key, value, cause) -> logger.info("Removal: {} - {} = {}", cause, key, value))
                    .build();

            return new CaffeineCache<>(cacheManager, name, caffeineCache);
        };

        assertThrows(NullPointerException.class, () -> Caching.getCache("a", String.class, String.class), "cacheFactory required");

        System.setProperty(Caching.JAVAX_CACHE_CACHING_PROVIDER, SimpleCachingProvider.class.getName());

        try (CachingProvider cachingProvider = Caching.getCachingProvider()) {
            assertNotNull(cachingProvider);
            assertInstanceOf(SimpleCachingProvider.class, cachingProvider);

            ((SimpleCachingProvider) cachingProvider).setCacheFactory(cacheFactory);

            assertNotNull(Caching.getCache("a", String.class, String.class));

            try (CacheManager cacheManager = cachingProvider.getCacheManager()) {
                assertNotNull(cacheManager);
                assertInstanceOf(DefaultCacheManager.class, cacheManager);

                for (String name : List.of("a", "b")) {
                    try (Cache<?, ?> cache = cacheManager.getCache(name)) {
                        assertNotNull(cache);

                        assertInstanceOf(CaffeineCache.class, cache.unwrap(CaffeineCache.class));

                        assertInstanceOf(com.github.benmanes.caffeine.cache.Cache.class, cache.unwrap(com.github.benmanes.caffeine.cache.Cache.class));
                    }

                    assertTrue(cacheManager.getCache(name).isClosed());
                }

                for (String name : List.of("a", "b")) {
                    try (Cache<?, ?> cache = cacheManager.createCache(name, null)) {
                        assertNotNull(cache);

                        assertNotNull(cache);

                        assertInstanceOf(CaffeineCache.class, cache.unwrap(CaffeineCache.class));

                        assertInstanceOf(com.github.benmanes.caffeine.cache.Cache.class, cache.unwrap(com.github.benmanes.caffeine.cache.Cache.class));

                        assertTrue(cacheManager.getCache(name).isClosed());
                    }
                }
            }
        }
    }
}
