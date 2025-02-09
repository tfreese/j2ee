// Created: 08 Feb. 2025
package de.freese.jcache.spi;

import static javax.cache.configuration.OptionalFeature.STORE_BY_REFERENCE;

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
public final class SimpleCachingProvider implements CachingProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private BiFunction<CacheManager, String, Cache<Object, Object>> cacheFactory;

    @Override
    public void close() {
        getLogger().debug("close");

        getCacheManager().close();
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
        return getCacheManager();
    }

    @Override
    public CacheManager getCacheManager() {
        Objects.requireNonNull(cacheFactory, "cacheFactory required");

        return new DefaultCacheManager(this, cacheFactory);
    }

    @Override
    public CacheManager getCacheManager(final URI uri, final ClassLoader classLoader) {
        return getCacheManager();
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Properties getDefaultProperties() {
        return new Properties();
    }

    @Override
    public URI getDefaultURI() {
        return URI.create(getClass().getSimpleName());
    }

    @Override
    public boolean isSupported(final OptionalFeature optionalFeature) {
        return optionalFeature == STORE_BY_REFERENCE;
    }

    public void setCacheFactory(final BiFunction<CacheManager, String, Cache<Object, Object>> cacheFactory) {
        this.cacheFactory = Objects.requireNonNull(cacheFactory, "cacheFactory required");
    }

    private Logger getLogger() {
        return logger;
    }
}
