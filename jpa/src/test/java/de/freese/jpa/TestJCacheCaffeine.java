// Created: 06 Juli 2024
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.freese.jpa.cache.JCacheManager;
import de.freese.jpa.cache.JCachingProvider;

/**
 * @author Thomas Freese
 */
class TestJCacheCaffeine {

    @BeforeAll
    static void beforeAll() {
        Caching.setDefaultClassLoader(Thread.currentThread().getContextClassLoader());
    }

    @Test
    void testJCache() {
        final CachingProvider cachingProvider = Caching.getCachingProvider();

        assertNotNull(cachingProvider);
        assertInstanceOf(JCachingProvider.class, cachingProvider);

        final CacheManager cacheManager = cachingProvider.getCacheManager(URI.create("myCacheConfig.properties"), Thread.currentThread().getContextClassLoader());
        assertNotNull(cacheManager);
        assertInstanceOf(JCacheManager.class, cacheManager);

        cachingProvider.close();
    }

    @Test
    void testJCacheDefault() {
        final CachingProvider cachingProvider = Caching.getCachingProvider();

        assertNotNull(cachingProvider);
        assertInstanceOf(JCachingProvider.class, cachingProvider);

        final CacheManager cacheManager = cachingProvider.getCacheManager();
        assertNotNull(cacheManager);
        assertInstanceOf(JCacheManager.class, cacheManager);

        cachingProvider.close();
    }
}
