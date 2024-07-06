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

import de.freese.jcache.configuration.GenericConfiguration;

/**
 * Generic CacheManager with CacheFactory.<br>
 * Inspired by CacheManagerImpl (com.github.ben-manes.caffeine:jcache).<br>
 *
 * @author Thomas Freese
 */
public final class GenericCacheManager implements CacheManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericCacheManager.class);
    private final BiFunction<CacheManager, String, Cache<?, ?>> cacheFactory;
    private final Map<String, Cache<?, ?>> cacheMap = new ConcurrentHashMap<>(16);
    private final GenericConfiguration configuration;

    private boolean closed;

    public GenericCacheManager(final BiFunction<CacheManager, String, Cache<?, ?>> cacheFactory, final GenericConfiguration configuration) {
        super();

        this.cacheFactory = Objects.requireNonNull(cacheFactory, "cacheFactory required");
        this.configuration = Objects.requireNonNull(configuration, "configuration required");
    }

    @Override
    public void close() {
        closed = true;

        LOGGER.debug("close");

        cacheMap.keySet().forEach(this::destroyCache);

        cacheMap.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(final String cacheName, final C configuration) throws IllegalArgumentException {
        if (isClosed()) {
            throw new IllegalStateException("CacheManager is closed");
        }

        return (Cache<K, V>) cacheMap.computeIfAbsent(cacheName, key -> {
            LOGGER.info("create missing cache: {}", cacheName);

            return cacheFactory.apply(this, key);
        });
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
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void enableStatistics(final String cacheName, final boolean enabled) {
        throw new IllegalStateException("not implemented");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Cache<K, V> getCache(final String cacheName, final Class<K> keyType, final Class<V> valueType) {
        if (isClosed()) {
            throw new IllegalStateException("CacheManager is closed");
        }

        return (Cache<K, V>) cacheMap.get(cacheName);
    }

    @Override
    public <K, V> Cache<K, V> getCache(final String cacheName) {
        return getCache(cacheName, null, null);
    }

    @Override
    public Iterable<String> getCacheNames() {
        return Set.copyOf(cacheMap.keySet());
    }

    @Override
    public CachingProvider getCachingProvider() {
        if (configuration != null) {
            return configuration.getCachingProvider();
        }

        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        if (configuration != null) {
            return configuration.getClassLoader();
        }

        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Properties getProperties() {
        if (configuration != null) {
            return configuration.getProperties();
        }

        return null;
    }

    @Override
    public URI getURI() {
        if (configuration != null) {
            return configuration.getUri();
        }

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
