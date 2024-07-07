// Created: 06 Juli 2024
package de.freese.jcache;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.freese.jcache.spi.GenericCacheManager;
import de.freese.jcache.spi.GenericCachingProvider;

/**
 * @author Thomas Freese
 */
class TestGenericJCache {

    @BeforeAll
    static void beforeAll() {
        Caching.setDefaultClassLoader(Thread.currentThread().getContextClassLoader());
    }

    @Test
    void testCaffeine() throws Exception {
        final URL url = Caching.getDefaultClassLoader().getResource("caffeineCacheConfig.properties");
        assertNotNull(url);

        try (CachingProvider cachingProvider = Caching.getCachingProvider()) {
            assertNotNull(cachingProvider);
            assertInstanceOf(GenericCachingProvider.class, cachingProvider);

            final CacheManager cacheManager = cachingProvider.getCacheManager(url.toURI(), Caching.getDefaultClassLoader());
            assertNotNull(cacheManager);
            assertInstanceOf(GenericCacheManager.class, cacheManager);

            for (String name : List.of("a", "b", "c")) {
                final Cache<?, ?> cache = cacheManager.getCache(name);

                if ("c".contains(name)) {
                    assertNull(cache);
                }
                else {
                    assertNotNull(cache);
                }
            }

            for (String name : List.of("a", "b", "c")) {
                final Cache<?, ?> cache = cacheManager.createCache(name, null);
                assertNotNull(cache);
                assertInstanceOf(CaffeineCache.class, cache);
                assertInstanceOf(com.github.benmanes.caffeine.cache.Cache.class, cache.unwrap(com.github.benmanes.caffeine.cache.Cache.class));
            }
        }
    }

    @Test
    void testDefault() {
        try (CachingProvider cachingProvider = Caching.getCachingProvider()) {
            assertNotNull(cachingProvider);
            assertInstanceOf(GenericCachingProvider.class, cachingProvider);

            final CacheManager cacheManager = cachingProvider.getCacheManager();
            assertNotNull(cacheManager);
            assertInstanceOf(GenericCacheManager.class, cacheManager);

            for (String name : List.of("a", "b", "c")) {
                final Cache<?, ?> cache = cacheManager.getCache(name);

                if ("c".contains(name)) {
                    assertNull(cache);
                }
                else {
                    assertNotNull(cache);
                }
            }

            for (String name : List.of("a", "b", "c")) {
                final Cache<?, ?> cache = cacheManager.createCache(name, null);
                assertNotNull(cache);
                assertInstanceOf(MapCache.class, cache);
            }

            Cache<?, ?> cache = cacheManager.getCache("a");
            assertNotNull(cache);
            assertInstanceOf(TreeMap.class, cache.unwrap(TreeMap.class));

            cache = cacheManager.getCache("b");
            assertNotNull(cache);
            assertInstanceOf(LinkedHashMap.class, cache.unwrap(LinkedHashMap.class));

            // Default
            cache = cacheManager.getCache("c");
            assertNotNull(cache);
            assertInstanceOf(HashMap.class, cache.unwrap(HashMap.class));
        }
    }
}
