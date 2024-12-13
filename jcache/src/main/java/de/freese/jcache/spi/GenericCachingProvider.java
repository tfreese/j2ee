// Created: 06 Juli 2024
package de.freese.jcache.spi;

import static javax.cache.configuration.OptionalFeature.STORE_BY_REFERENCE;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.function.BiFunction;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jcache.CacheFactory;
import de.freese.jcache.configuration.GenericConfiguration;

/**
 * Generic CachingProvider for multiple Backends.<br>
 * Inspired by CaffeineCachingProvider (com.github.ben-manes.caffeine:jcache).<br>
 *
 * @author Thomas Freese
 */
public final class GenericCachingProvider implements CachingProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericCachingProvider.class);

    private final Map<ClassLoader, Map<URI, CacheManager>> cacheManagers = WeakHashMap.newWeakHashMap(1);

    @Override
    public void close() {
        LOGGER.debug("close()");

        synchronized (cacheManagers) {
            for (ClassLoader classLoader : new ArrayList<>(cacheManagers.keySet())) {
                close(classLoader);
            }
        }
    }

    @Override
    public void close(final ClassLoader classLoader) {
        LOGGER.debug("close(ClassLoader)");

        synchronized (cacheManagers) {
            final ClassLoader managerClassLoader = getManagerClassLoader(classLoader);
            final Map<URI, CacheManager> cacheManagersByUri = cacheManagers.remove(managerClassLoader);

            if (cacheManagersByUri != null) {
                for (CacheManager cacheManager : cacheManagersByUri.values()) {
                    cacheManager.close();
                }
            }
        }
    }

    @Override
    public void close(final URI uri, final ClassLoader classLoader) {
        LOGGER.debug("close(URI, ClassLoader)");

        synchronized (cacheManagers) {
            final ClassLoader managerClassLoader = getManagerClassLoader(classLoader);
            final Map<URI, CacheManager> cacheManagersByURI = cacheManagers.get(managerClassLoader);

            if (cacheManagersByURI != null) {
                final CacheManager cacheManager = cacheManagersByURI.remove(getManagerUri(uri));

                if (cacheManager != null) {
                    cacheManager.close();
                }
                if (cacheManagersByURI.isEmpty()) {
                    cacheManagers.remove(managerClassLoader);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("java:S2095")
    public CacheManager getCacheManager(final URI uri, final ClassLoader classLoader, final Properties properties) {
        final URI managerURI = getManagerUri(uri);
        final ClassLoader managerClassLoader = getManagerClassLoader(classLoader);

        if (cacheManagers.computeIfAbsent(managerClassLoader, key -> new HashMap<>()).get(managerURI) != null) {
            return cacheManagers.get(managerClassLoader).get(managerURI);
        }

        final GenericConfiguration configuration;

        try {
            if (managerURI.equals(getDefaultURI())) {
                final URL defaultUrl = classLoader.getResource("defaultCacheConfig.properties");
                configuration = GenericConfiguration.from(defaultUrl.toURI());
            }
            else {
                configuration = GenericConfiguration.from(uri);
            }

            configuration.setClassLoader(classLoader)
                    .setProperties(properties)
                    .setCachingProvider(this);

            final BiFunction<CacheManager, String, Cache<?, ?>> cacheFactory = CacheFactory.from(configuration);

            final CacheManager cacheManager = new GenericCacheManager(cacheFactory, configuration);

            // Create registered Caches.
            configuration.getCacheBackends().keySet().forEach(cacheName -> cacheManager.createCache(cacheName, null));

            cacheManagers.computeIfAbsent(managerClassLoader, key -> new HashMap<>()).put(managerURI, cacheManager);

            return cacheManager;
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);

            if (ex instanceof RuntimeException rex) {
                throw rex;
            }
            else {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public CacheManager getCacheManager(final URI uri, final ClassLoader classLoader) {
        return getCacheManager(uri, classLoader, getDefaultProperties());
    }

    @Override
    public CacheManager getCacheManager() {
        return getCacheManager(getDefaultURI(), getDefaultClassLoader());
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        final ClassLoader classLoader = Caching.getDefaultClassLoader();

        return classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader();
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

    private ClassLoader getManagerClassLoader(final ClassLoader classLoader) {
        return (classLoader == null) ? getDefaultClassLoader() : classLoader;
    }

    private URI getManagerUri(final URI uri) {
        return (uri == null) ? getDefaultURI() : uri;
    }
}
