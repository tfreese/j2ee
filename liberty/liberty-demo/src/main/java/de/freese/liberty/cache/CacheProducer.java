// Created: 17 Jan. 2026
package de.freese.liberty.cache;

import java.time.Duration;
import java.util.OptionalLong;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResult;
import javax.cache.spi.CachingProvider;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;

/**
 * @author Thomas Freese
 */
// @Singleton
// @Startup
@ApplicationScoped
public class CacheProducer {
    private static <K, V> CaffeineConfiguration<K, V> defaultConfig(final Class<K> keyType, final Class<V> valueType, final Duration expireAfterWrite) {
        return new CaffeineConfiguration<K, V>()
                .setTypes(keyType, valueType)
                .setStoreByValue(false)
                .setStatisticsEnabled(true)
                .setExpireAfterWrite(OptionalLong.of(expireAfterWrite.toNanos()));
    }

    private final CacheManager cacheManager;
    private final CachingProvider provider;

    private Cache<String, String> countryByCode;

    @Resource(lookup = "java:comp/DefaultManagedExecutorService")
    private ExecutorService executorService;

    public CacheProducer() {
        super();

        this.provider = Caching.getCachingProvider();
        this.cacheManager = provider.getCacheManager();
    }

    /**
     * @param expireAfterWriteSupplier Only required for cache creation (first call)
     **/
    public <K, V> Cache<K, V> getCache(final String name, final Class<K> keyType, final Class<V> valueType, final Supplier<Duration> expireAfterWriteSupplier) {
        Cache<K, V> cache = cacheManager.getCache(name, keyType, valueType);

        if (cache == null) {
            final CaffeineConfiguration<K, V> configuration = defaultConfig(keyType, valueType, expireAfterWriteSupplier.get())
                    .setExecutorFactory(() -> executorService);

            cache = cacheManager.createCache(name, configuration);
        }

        return cache;
    }

    public Cache<String, String> getCountryByCode() {
        if (countryByCode == null) {
            countryByCode = getCache("countryByCode", String.class, String.class, () -> Duration.ofMinutes(1L));
        }

        return countryByCode;
    }

    /**
     * For Injection and using Annotations in javax.cache.annotation, like {@link CacheResult}.
     **/
    @Produces
    @ApplicationScoped
    public CacheManager produceCacheManager() {
        return cacheManager;
    }

    @PreDestroy
    public void shutdown() {
        if (cacheManager != null) {
            cacheManager.close();
        }

        if (provider != null) {
            provider.close();
        }
    }
}
