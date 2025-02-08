// Created: 08 Feb. 2025
package de.freese.jcache.wrapper;

import static javax.cache.configuration.OptionalFeature.STORE_BY_REFERENCE;

import java.net.URI;
import java.util.Properties;

import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;

/**
 * @author Thomas Freese
 */
final class CachingProviderWrapper implements CachingProvider {

    CachingProviderWrapper() {
        super();
    }

    @Override
    public void close() {
        // TODO
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
    public CacheManager getCacheManager(final URI uri, final ClassLoader classLoader) {
        return getCacheManager();
    }

    @Override
    public CacheManager getCacheManager() {
        // TODO
        return null;
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
}
