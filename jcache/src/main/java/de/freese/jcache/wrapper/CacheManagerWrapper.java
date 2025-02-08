// Created: 08 Feb. 2025
package de.freese.jcache.wrapper;

import java.net.URI;
import java.util.Properties;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

/**
 * @author Thomas Freese
 */
final class CacheManagerWrapper implements CacheManager {
    private boolean closed;

    CacheManagerWrapper() {
        super();
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(final String cacheName, final C configuration) throws IllegalArgumentException {
        // TODO
        return null;
    }

    @Override
    public void destroyCache(final String cacheName) {
        // TODO
    }

    @Override
    public void enableManagement(final String cacheName, final boolean enabled) {
        // Empty
    }

    @Override
    public void enableStatistics(final String cacheName, final boolean enabled) {
        // Empty
    }

    @Override
    public <K, V> Cache<K, V> getCache(final String cacheName, final Class<K> keyType, final Class<V> valueType) {
        return getCache(cacheName);
    }

    @Override
    public <K, V> Cache<K, V> getCache(final String cacheName) {
        // TODO
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
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Properties getProperties() {
        return new Properties();
    }

    @Override
    public URI getURI() {
        return URI.create(getClass().getSimpleName());
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public <T> T unwrap(final Class<T> clazz) {
        if (clazz.isAssignableFrom(getClass())) {
            return clazz.cast(this);
        }

        throw new IllegalArgumentException("Unwrapping to '" + clazz + "' is not supported");
    }
}
