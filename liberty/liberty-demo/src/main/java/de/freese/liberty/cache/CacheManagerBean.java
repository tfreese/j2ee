// Created: 17 Jan. 2026
package de.freese.liberty.cache;

import java.time.Duration;
import java.util.Objects;
import java.util.OptionalLong;
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
public class CacheManagerBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManagerBean.class);

    public static CacheManagerBean getInstance() {
        return CDI.current().select(CacheManagerBean.class).get();
    }

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
    public com.github.benmanes.caffeine.cache.Cache<String, String> getCountryByCodeCache() {
        return CacheManager.getCache("countryByCode");
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

        Objects.requireNonNull(CacheManager.createCache("countryByCode", Duration.ofMinutes(10)));
    }

    @PreDestroy
    void beforeShutdown() {
        CacheManager.close();

        LOGGER.info("Closing all JCaches");

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
