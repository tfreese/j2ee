// Created: 06 Juli 2024
package de.freese.jcache;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.cache.CacheManager;

import de.freese.jcache.spi.AbstractCache;

/**
 * @author Thomas Freese
 */
public final class MapCache<K, V> extends AbstractCache<K, V> {
    private final Map<K, V> cache;

    public MapCache(final CacheManager cacheManager, final String name, final Map<K, V> cache) {
        super(cacheManager, name);

        this.cache = Objects.requireNonNull(cache, "cache required");
    }

    @Override
    public void clear() {
        getLogger().debug("clear");

        cache.clear();
    }

    @Override
    public void close() {
        setClosed();

        getLogger().debug("close");

        cache.clear();
    }

    @Override
    public boolean containsKey(final K key) {
        return cache.containsKey(key);
    }

    @Override
    public V get(final K key) {
        return cache.get(key);
    }

    @Override
    public V getAndRemove(final K key) {
        return cache.remove(key);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        requiredNotClosed();

        return new Iterator<>() {
            private final Iterator<Map.Entry<K, V>> iterator = cache.entrySet().iterator();

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
        requiredNotClosed();

        cache.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        requiredNotClosed();

        cache.putAll(map);
    }

    @Override
    public boolean remove(final K key) {
        final boolean contains = containsKey(key);

        cache.remove(key);

        return contains;
    }

    @Override
    public boolean remove(final K key, final V oldValue) {
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
        keys.forEach(cache::remove);
    }

    @Override
    public void removeAll() {
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
