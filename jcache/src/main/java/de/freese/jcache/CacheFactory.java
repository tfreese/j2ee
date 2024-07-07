// Created: 06 Juli 2024
package de.freese.jcache;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.function.BiFunction;

import javax.cache.Cache;
import javax.cache.CacheManager;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.jcache.configuration.GenericConfiguration;

/**
 * @author Thomas Freese
 */
public final class CacheFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheFactory.class);

    public static BiFunction<CacheManager, String, Cache<?, ?>> from(final GenericConfiguration configuration) {
        if ("de.freese.jcache.MapCache".equals(configuration.getCacheImplementation())) {
            return fromMap(configuration);
        }
        else if ("de.freese.jcache.CaffeineCache".equals(configuration.getCacheImplementation())) {
            return fromCaffeine(configuration);
        }

        throw new UnsupportedOperationException("unknown cache implementation: " + configuration.getCacheImplementation());
    }

    private static BiFunction<CacheManager, String, Cache<?, ?>> fromCaffeine(final GenericConfiguration configuration) {
        final Map<String, String> backends = configuration.getCacheBackends();

        return (cacheManager, name) -> {
            final String backend = backends.computeIfAbsent(name, key -> backends.get("default"));

            if (backend == null) {
                throw new IllegalStateException("no backend or no default configured for cache: " + name);
            }

            final Caffeine<Object, Object> caffeine = Caffeine.from(backend)
                    .evictionListener((key, value, cause) -> LOGGER.debug("Eviction: {} - {} = {}", cause, key, value))
                    .removalListener((key, value, cause) -> LOGGER.debug("Removal: {} - {} = {}", cause, key, value));

            return new CaffeineCache<>(cacheManager, name, caffeine.build());
        };
    }

    private static BiFunction<CacheManager, String, Cache<?, ?>> fromMap(final GenericConfiguration configuration) {
        final Map<String, String> backends = configuration.getCacheBackends();

        return (cacheManager, name) -> {
            final String backend = backends.computeIfAbsent(name, key -> backends.get("default"));

            if (backend == null) {
                throw new IllegalStateException("no backend or no default configured for cache: " + name);
            }

            try {
                final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
                final Class<?> mapClazz = lookup.findClass(backend);
                final Constructor<?> mapConstructor = mapClazz.getConstructor();
                final Map<?, ?> map = (Map<?, ?>) mapConstructor.newInstance();

                return new MapCache<>(cacheManager, name, map);
            }
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    private CacheFactory() {
        super();
    }
}
