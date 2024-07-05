// Created: 05 Juli 2024
package de.freese.jpa.cache;

import java.net.URI;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiFunction;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public final class JCachingProvider implements CachingProvider {
    static final Properties DEFAULT_PROPERTIES = new Properties();

    private static final Logger LOGGER = LoggerFactory.getLogger(JCachingProvider.class);

    private static BiFunction<CacheManager, String, Cache<?, ?>> cacheFactory;

    public static void setCacheFactory(final BiFunction<CacheManager, String, Cache<?, ?>> cacheFactory) {
        JCachingProvider.cacheFactory = Objects.requireNonNull(cacheFactory, "cacheFactory required");
    }

    private CacheManager cacheManager;
    private boolean closed;

    @Override
    public void close() {
        closed = true;

        LOGGER.info("close");
    }

    @Override
    public void close(final ClassLoader classLoader) {
        close();
    }

    @Override
    public void close(final URI uri, final ClassLoader classLoader) {
        close();
    }

    @Override
    public CacheManager getCacheManager(final URI uri, final ClassLoader classLoader, final Properties properties) {
        if (closed) {
            throw new IllegalStateException("CachingProvider is closed");
        }

        synchronized (this) {
            if (cacheManager == null) {
                cacheManager = new JCacheManager(cacheFactory, this);
            }
        }

        return cacheManager;
    }

    @Override
    public CacheManager getCacheManager(final URI uri, final ClassLoader classLoader) {
        return getCacheManager(uri, classLoader, null);
    }

    @Override
    public CacheManager getCacheManager() {
        return getCacheManager(null, null);
    }

    @Override
    public ClassLoader getDefaultClassLoader() {

        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Properties getDefaultProperties() {
        return DEFAULT_PROPERTIES;
    }

    @Override
    public URI getDefaultURI() {
        return URI.create(getClass().getSimpleName());
    }

    @Override
    public boolean isSupported(final OptionalFeature optionalFeature) {
        return true;
    }
}
