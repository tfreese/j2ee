// Created: 04 Feb. 2026
package de.freese.liberty.cache;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("unchecked")
public final class CacheManager {
    private static final Map<String, Cache<?, ?>> CACHES = new ConcurrentSkipListMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

    public static void close() {
        LOGGER.info("Closing all caches");

        CACHES.forEach((name, cache) -> {
            if (LOGGER.isInfoEnabled()) {
                final CacheStats cacheStats = cache.stats();
                final String stats = "hitCount=%d, hitRate=%f%%, missCount=%d, missRate=%f%%, evictionCount=%d, requestCount=%d".formatted(
                        cacheStats.hitCount(),
                        cacheStats.hitRate() * 100D,
                        cacheStats.missCount(),
                        cacheStats.missRate() * 100D,
                        cacheStats.evictionCount(),
                        cacheStats.requestCount()
                );

                LOGGER.info("Closing Cache: {}, Size={}, Stats=[{}]", name, cache.estimatedSize(), stats);
            }

            cache.invalidateAll();
            cache.cleanUp();
        });

        CACHES.clear();
    }

    public static <K, V> Cache<K, V> createCache(final String name, final Duration expiry) {
        return createCache(name, expiry, null);
    }

    /**
     * @param cacheConfigurer Optional
     */
    public static <K, V> Cache<K, V> createCache(final String name, final Duration expiry, @Nullable final Consumer<Caffeine<Object, Object>> cacheConfigurer) {
        Cache<K, V> cache = (Cache<K, V>) CACHES.get(name);

        if (cache != null) {
            throw new IllegalStateException("Cache already exists: " + name);
        }

        final Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(expiry)
                .recordStats();

        if (cacheConfigurer != null) {
            cacheConfigurer.accept(caffeine);
        }

        cache = caffeine.build();

        CACHES.put(name, cache);

        return cache;
    }

    public static <K, V> Cache<K, V> getCache(final String name) {
        final Cache<K, V> cache = (Cache<K, V>) CACHES.get(name);

        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + name);
        }

        return cache;
    }

    private CacheManager() {
        super();
    }
}
