// Created: 06 Juli 2024
package de.freese.jcache;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiFunction;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.junit.jupiter.api.Test;

import de.freese.jcache.spi.DefaultCacheManager;
import de.freese.jcache.spi.SimpleCachingProvider;

/**
 * @author Thomas Freese
 */
class TestJCacheMap {

    // CachingProvider cachingProvider = Caching.getCachingProvider(SimpleCachingProvider.class.getName())

    @Test
    void testMap() {
        final BiFunction<CacheManager, String, Cache<Object, Object>> cacheFactory = (cacheManager, name) -> {
            if (name.equals("a")) {
                return new MapCache<>(cacheManager, name, new TreeMap<>());
            }

            return new MapCache<>(cacheManager, name, new HashMap<>());
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

                        assertInstanceOf(MapCache.class, cache.unwrap(MapCache.class));

                        if ("a".contains(name)) {
                            assertInstanceOf(TreeMap.class, cache.unwrap(TreeMap.class));
                        }
                        else {
                            assertInstanceOf(HashMap.class, cache.unwrap(HashMap.class));
                        }
                    }

                    assertTrue(cacheManager.getCache(name).isClosed());
                }

                for (String name : List.of("a", "b")) {
                    try (Cache<?, ?> cache = cacheManager.createCache(name, null)) {
                        assertNotNull(cache);

                        assertNotNull(cache);

                        assertInstanceOf(MapCache.class, cache.unwrap(MapCache.class));

                        if ("a".contains(name)) {
                            assertInstanceOf(TreeMap.class, cache.unwrap(TreeMap.class));
                        }
                        else {
                            assertInstanceOf(HashMap.class, cache.unwrap(HashMap.class));
                        }

                        assertTrue(cacheManager.getCache(name).isClosed());
                    }
                }
            }
        }
    }
}
