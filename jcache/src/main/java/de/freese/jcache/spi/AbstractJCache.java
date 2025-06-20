// Created: 05 Juli 2024
package de.freese.jcache.spi;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public abstract class AbstractJCache<K, V> implements Cache<K, V> {
    private final CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String name;

    private boolean closed;

    protected AbstractJCache(final CacheManager cacheManager, final String name) {
        super();

        this.cacheManager = Objects.requireNonNull(cacheManager, "cacheManager required");
        this.name = Objects.requireNonNull(name, "name required");
    }

    @Override
    public void deregisterCacheEntryListener(final CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        logger.warn("not implemented");
    }

    @Override
    public Map<K, V> getAll(final Set<? extends K> keys) {
        validateNotClosed();

        final Map<K, V> map = new HashMap<>();

        keys.forEach(key -> {
            final V value = get(key);

            if (value != null) {
                map.put(key, value);
            }
        });

        return map;
    }

    @Override
    public V getAndPut(final K key, final V value) {
        validateNotClosed();

        final V oldValue = get(key);
        put(key, value);

        return oldValue;
    }

    @Override
    public V getAndReplace(final K key, final V value) {
        validateNotClosed();

        if (containsKey(key)) {
            final V oldValue = get(key);
            put(key, value);

            return oldValue;
        }
        else {
            return null;
        }
    }

    @Override
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public <C extends Configuration<K, V>> C getConfiguration(final Class<C> clazz) {
        logger.warn("not implemented");

        return null;
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T> T invoke(final K key, final EntryProcessor<K, V, T> entryProcessor, final Object... arguments) throws EntryProcessorException {
        logger.warn("not implemented");

        return null;
    }

    @Override
    public <T> Map<K, EntryProcessorResult<T>> invokeAll(final Set<? extends K> keys, final EntryProcessor<K, V, T> entryProcessor, final Object... arguments) {
        logger.warn("not implemented");

        return Map.of();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void loadAll(final Set<? extends K> keys, final boolean replaceExistingValues, final CompletionListener completionListener) {
        logger.warn("not implemented");
    }

    @Override
    public boolean putIfAbsent(final K key, final V value) {
        validateNotClosed();

        if (!containsKey(key)) {
            put(key, value);

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void registerCacheEntryListener(final CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        logger.warn("not implemented");
    }

    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        validateNotClosed();

        if (containsKey(key) && Objects.equals(get(key), oldValue)) {
            put(key, newValue);

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean replace(final K key, final V value) {
        validateNotClosed();

        if (containsKey(key)) {
            put(key, value);

            return true;
        }
        else {
            return false;
        }
    }

    protected void setClosed() {
        closed = true;
    }

    protected void validateNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("Cache is closed: " + getName());
        }
    }
}
