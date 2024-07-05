// Created: 04 Juli 2024
package de.freese.jpa.cache;

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
public final class JCacheManager implements CacheManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(JCacheManager.class);

    private final BiFunction<CacheManager, String, Cache<?, ?>> cacheFactory;
    private final Map<String, Cache<?, ?>> cacheMap = new ConcurrentHashMap<>(16);
    private final CachingProvider cachingProvider;

    private boolean closed;

    public JCacheManager(final BiFunction<CacheManager, String, Cache<?, ?>> cacheFactory) {
        super();

        this.cacheFactory = Objects.requireNonNull(cacheFactory, "cacheFactory required");
        this.cachingProvider = null;
    }

    public JCacheManager(final BiFunction<CacheManager, String, Cache<?, ?>> cacheFactory, final CachingProvider cachingProvider) {
        super();

        this.cacheFactory = Objects.requireNonNull(cacheFactory, "cacheFactory required");
        this.cachingProvider = Objects.requireNonNull(cachingProvider, "cachingProvider required");
    }

    @Override
    public void close() {
        closed = true;

        LOGGER.info("close");

        cacheMap.keySet().forEach(this::destroyCache);

        cacheMap.clear();
    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(final String cacheName, final C configuration) throws IllegalArgumentException {
        return getCache(cacheName);
    }

    @Override
    public void destroyCache(final String cacheName) {
        LOGGER.info("destroyCache: {}", cacheName);

        final Cache<?, ?> cache = cacheMap.remove(cacheName);

        if (cache != null) {
            cache.clear();
            cache.close();
        }
    }

    @Override
    public void enableManagement(final String cacheName, final boolean enabled) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void enableStatistics(final String cacheName, final boolean enabled) {
        throw new IllegalStateException("not implemented");
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
            LOGGER.info("create missing cache: {}", cacheName);

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
        if (cachingProvider != null) {
            return cachingProvider.getDefaultClassLoader();
        }
        else {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    @Override
    public Properties getProperties() {
        if (cachingProvider != null) {
            return cachingProvider.getDefaultProperties();
        }
        else {
            return JCachingProvider.DEFAULT_PROPERTIES;
        }
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
