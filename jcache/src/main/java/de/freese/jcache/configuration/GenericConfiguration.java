// Created: 06 Juli 2024
package de.freese.jcache.configuration;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.cache.spi.CachingProvider;

/**
 * @author Thomas Freese
 */
public final class GenericConfiguration {
    // implements Configuration<Object, Object>

    public static GenericConfiguration from(final URI uri) throws Exception {
        final Properties properties = new Properties();

        try (InputStream inputStream = uri.toURL().openStream()) {
            properties.load(inputStream);
        }

        final GenericConfiguration configuration = new GenericConfiguration(uri);
        configuration.setCreateCachesLazy(Boolean.parseBoolean(properties.getProperty("create.caches.lazy")));
        configuration.setCacheImplementation(properties.getProperty("cache.implementation"));

        properties.entrySet().stream()
                .filter(entry -> ((String) entry.getKey()).startsWith("cache.backend"))
                .forEach(entry -> {
                    final String name = ((String) entry.getKey()).replace("cache.backend.", "");
                    configuration.addCacheBackend(name, (String) entry.getValue());
                });

        return configuration;
    }

    private final Map<String, String> cacheBackends = new HashMap<>();
    private final URI uri;

    private String cacheImplementation;
    private CachingProvider cachingProvider;
    private ClassLoader classLoader;
    private boolean createCachesLazy = true;
    private Properties properties;

    private GenericConfiguration(final URI uri) {
        super();

        this.uri = uri;
    }

    public Map<String, String> getCacheBackends() {
        return cacheBackends;
    }

    public String getCacheImplementation() {
        return cacheImplementation;
    }

    public CachingProvider getCachingProvider() {
        return cachingProvider;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Properties getProperties() {
        return properties;
    }

    public URI getUri() {
        return uri;
    }

    public boolean isCreateCachesLazy() {
        return createCachesLazy;
    }

    public void setCachingProvider(final CachingProvider cachingProvider) {
        this.cachingProvider = cachingProvider;
    }

    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setProperties(final Properties properties) {
        this.properties = properties;
    }

    private void addCacheBackend(final String name, final String value) {
        this.cacheBackends.put(name, value);
    }

    private void setCacheImplementation(final String cacheImplementation) {
        this.cacheImplementation = cacheImplementation;
    }

    private void setCreateCachesLazy(final boolean createCachesLazy) {
        this.createCachesLazy = createCachesLazy;
    }
}
