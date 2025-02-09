// Created: 06 Juli 2024
package de.freese.jcache.spi;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public final class DefaultCacheManager implements CacheManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCacheManager.class);

    private final BiFunction<CacheManager, String, Cache<Object, Object>> cacheFactory;
    private final Map<String, Cache<Object, Object>> cacheMap = new ConcurrentHashMap<>(16);
    private final CachingProvider cachingProvider;

    private boolean closed;

    public DefaultCacheManager(final CachingProvider cachingProvider, final BiFunction<CacheManager, String, Cache<Object, Object>> cacheFactory) {
        super();

        this.cachingProvider = Objects.requireNonNull(cachingProvider, "cachingProvider required");
        this.cacheFactory = Objects.requireNonNull(cacheFactory, "cacheFactory required");
    }

    @Override
    public void close() {
        closed = true;

        LOGGER.debug("close");

        cacheMap.keySet().forEach(this::destroyCache);

        cacheMap.clear();
    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(final String cacheName, final C configuration) throws IllegalArgumentException {
        return getCache(cacheName);
    }

    @Override
    public void destroyCache(final String cacheName) {
        LOGGER.debug("destroyCache: {}", cacheName);

        final Cache<?, ?> cache = cacheMap.remove(cacheName);

        if (cache != null) {
            cache.clear();
            cache.close();
        }
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

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Cache<K, V> getCache(final String cacheName) {
        if (isClosed()) {
            throw new IllegalStateException("CacheManager is closed");
        }

        return (Cache<K, V>) cacheMap.computeIfAbsent(cacheName, key -> {
            LOGGER.info("create cache: {}", cacheName);

            return cacheFactory.apply(this, key);
        });
    }

    @Override
    public Iterable<String> getCacheNames() {
        return Set.copyOf(cacheMap.keySet());
    }

    @Override
    public CachingProvider getCachingProvider() {
        return cachingProvider;
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

        throw new IllegalArgumentException("Unwrapping to '" + clazz + "' is not a supported");
    }
}
