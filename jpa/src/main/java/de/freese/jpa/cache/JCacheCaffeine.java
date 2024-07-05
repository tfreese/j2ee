// Created: 04 Juli 2024
package de.freese.jpa.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.cache.CacheManager;

import com.github.benmanes.caffeine.cache.Cache;

/**
 * @author Thomas Freese
 */
public final class JCacheCaffeine<K, V> extends AbstractJCacheAdapter<K, V> {

    private final Cache<K, V> cache;

    public JCacheCaffeine(final CacheManager cacheManager, final String name, final Cache<K, V> cache) {
        super(cacheManager, name);

        this.cache = Objects.requireNonNull(cache, "cache required");
    }

    @Override
    public void clear() {
        getLogger().info("clear: {}", getName());

        cache.invalidateAll();
    }

    @Override
    public void close() {
        setClosed();

        getLogger().info("close: {}", getName());

        cache.invalidateAll();
        cache.cleanUp();
    }

    @Override
    public boolean containsKey(final K key) {
        return cache.getIfPresent(key) != null;
    }

    @Override
    public V get(final K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public V getAndRemove(final K key) {
        if (containsKey(key)) {
            final V oldValue = get(key);
            cache.invalidate(key);

            return oldValue;
        }
        else {
            return null;
        }
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        requiredNotClosed();

        return new Iterator<>() {
            private final Iterator<Map.Entry<K, V>> iterator = cache.asMap().entrySet().iterator();

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

        cache.invalidate(key);

        return contains;
    }

    @Override
    public boolean remove(final K key, final V oldValue) {
        if (containsKey(key) && Objects.equals(get(key), oldValue)) {
            cache.invalidate(key);

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void removeAll(final Set<? extends K> keys) {
        cache.invalidateAll(keys);
    }

    @Override
    public void removeAll() {
        cache.invalidateAll();
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
