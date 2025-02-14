// Created: 06 Juli 2024
package de.freese.jcache.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.cache.CacheManager;

import de.freese.jcache.spi.AbstractJCache;
import de.freese.jcache.spi.CacheEntry;

/**
 * @author Thomas Freese
 */
public final class MapJCache<K, V> extends AbstractJCache<K, V> {
    private final Map<K, V> cache;

    public MapJCache(final CacheManager cacheManager, final String name, final Map<K, V> cache) {
        super(cacheManager, name);

        this.cache = Objects.requireNonNull(cache, "cache required");
    }

    @Override
    public void clear() {
        getLogger().debug("clear: {}", getName());

        cache.clear();
    }

    @Override
    public void close() {
        setClosed();

        getLogger().debug("close: {}", getName());

        cache.clear();
    }

    @Override
    public boolean containsKey(final K key) {
        validateNotClosed();

        return cache.containsKey(key);
    }

    @Override
    public V get(final K key) {
        validateNotClosed();

        return cache.get(key);
    }

    @Override
    public V getAndRemove(final K key) {
        validateNotClosed();

        return cache.remove(key);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        validateNotClosed();

        return new Iterator<>() {
            private final Iterator<Map.Entry<K, V>> iterator = Set.copyOf(cache.entrySet()).iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry<K, V> next() {
                return CacheEntry.of(iterator.next());
            }
        };
    }

    @Override
    public void put(final K key, final V value) {
        validateNotClosed();

        cache.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        validateNotClosed();

        cache.putAll(map);
    }

    @Override
    public boolean remove(final K key) {
        validateNotClosed();

        final V value = cache.remove(key);

        return value != null;
    }

    @Override
    public boolean remove(final K key, final V oldValue) {
        validateNotClosed();

        if (containsKey(key) && Objects.equals(get(key), oldValue)) {
            cache.remove(key);

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void removeAll(final Set<? extends K> keys) {
        validateNotClosed();

        keys.forEach(cache::remove);
    }

    @Override
    public void removeAll() {
        validateNotClosed();

        cache.clear();
    }

    @Override
    public <T> T unwrap(final Class<T> clazz) {
        if (clazz.isAssignableFrom(cache.getClass())) {
            return clazz.cast(cache);
        }

        if (clazz.isAssignableFrom(getClass())) {
            return clazz.cast(this);
        }

        throw new IllegalArgumentException("Unwrapping to '" + clazz + "' is not supported");
    }
}
