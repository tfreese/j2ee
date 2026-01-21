// Created: 17 Jan. 2026
package de.freese.liberty.cache;

import java.time.Duration;
import java.util.Map;
import java.util.OptionalLong;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import javax.cache.Caching;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.CDI;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Startup // Create on startup.
@Singleton
// @DependsOn({"OtherBean"})
// @ApplicationScoped // Create on first Access.
@SuppressWarnings("unchecked")
public class CacheManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

    public static CacheManager getInstance() {
        return CDI.current().select(CacheManager.class).get();
    }

    /**
     * Self provided Cache.
     **/
    private final Map<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();

    /**
     * JCache-API.
     **/
    private javax.cache.CacheManager cacheManager;
    /**
     * JCache-API.
     **/
    private javax.cache.spi.CachingProvider cachingProvider;

    @Resource(lookup = "java:comp/DefaultManagedExecutorService")
    private ExecutorService executorService;

    /**
     * JCache-API.
     **/
    private javax.cache.Cache<String, String> jCache;

    /**
     * Self provided Cache.
     **/
    @Produces // For Injection and using Annotations in javax.cache.annotation, like {@link CacheResult}.
    @NamedCache("countryByCode")
    @ApplicationScoped
    public Cache<String, String> getCountryByCodeCache() {
        return (Cache<String, String>) caches.computeIfAbsent("countryByCode", key -> {
            LOGGER.info("Create CountryByCode cache");

            return Caffeine.newBuilder()
                    .expireAfterWrite(Duration.ofMinutes(10))
                    .recordStats()
                    .executor(executorService)
                    .build();
        });
    }

    /**
     * JCache-API.
     **/
    @Produces
    @NamedCache("jCache")
    @ApplicationScoped
    public synchronized javax.cache.Cache<String, String> getJCache() {
        if (jCache == null) {
            LOGGER.info("Create JCache");

            jCache = cacheManager.getCache("jCache", String.class, String.class);

            if (jCache == null) {
                final CaffeineConfiguration<String, String> configuration = new CaffeineConfiguration<String, String>()
                        .setTypes(String.class, String.class)
                        .setStoreByValue(false)
                        .setStatisticsEnabled(true)
                        .setExpireAfterWrite(OptionalLong.of(Duration.ofMinutes(10).toNanos()))
                        .setExecutorFactory(() -> executorService);

                jCache = cacheManager.createCache("jCache", configuration);
            }
        }

        return jCache;
    }

    @PostConstruct
    void afterStartup() {
        LOGGER.info("Startup CacheManager");

        // JCache-API.
        this.cachingProvider = Caching.getCachingProvider();
        this.cacheManager = cachingProvider.getCacheManager();
    }

    @PreDestroy
    void beforeShutdown() {
        LOGGER.info("Closing all caches");

        caches.forEach((name, cache) -> {
            LOGGER.info("Closing Cache: {}, Size={}, Stats={}", name, cache.estimatedSize(), cache.stats());

            cache.invalidateAll();
            cache.cleanUp();
        });

        caches.clear();

        // JCache-API.
        if (jCache != null) {
            // Would be done by com.github.benmanes.caffeine.jcache.CacheManagerImpl automatically.
            jCache.clear();
            jCache.close();
        }

        if (cacheManager != null) {
            cacheManager.close();
        }

        if (cachingProvider != null) {
            cachingProvider.close();
        }
    }
}
