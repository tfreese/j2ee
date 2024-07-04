// Created: 04 Juli 2024
package de.freese.jpa.cache;

import java.net.URI;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

/**
 * @author Thomas Freese
 */
public class JCacheManagerCaffeine implements CacheManager {
    private final Map<String, com.github.benmanes.caffeine.cache.Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public void close() {

    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(final String s, final C c) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void destroyCache(final String s) {

    }

    @Override
    public void enableManagement(final String s, final boolean b) {

    }

    @Override
    public void enableStatistics(final String s, final boolean b) {

    }

    @Override
    public <K, V> Cache<K, V> getCache(final String s, final Class<K> aClass, final Class<V> aClass1) {
        return null;
    }

    @Override
    public <K, V> Cache<K, V> getCache(final String s) {
        return null;
    }

    @Override
    public Iterable<String> getCacheNames() {
        return null;
    }

    @Override
    public CachingProvider getCachingProvider() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public URI getURI() {
        return null;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public <T> T unwrap(final Class<T> aClass) {
        return null;
    }
}
