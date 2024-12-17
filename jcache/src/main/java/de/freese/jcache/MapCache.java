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
public final class MapCache extends AbstractCache<Object, Object> {
    private final Map<Object, Object> cache;

    public MapCache(final CacheManager cacheManager, final String name, final Map<Object, Object> cache) {
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
    public boolean containsKey(final Object key) {
        return cache.containsKey(key);
    }

    @Override
    public Object get(final Object key) {
        return cache.get(key);
    }

    @Override
    public Object getAndRemove(final Object key) {
        return cache.remove(key);
    }

    @Override
    public Iterator<Entry<Object, Object>> iterator() {
        requiredNotClosed();

        return new Iterator<>() {
            private final Iterator<Map.Entry<Object, Object>> iterator = cache.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry<Object, Object> next() {
                return CacheEntry.of(iterator.next());
            }
        };
    }

    @Override
    public void put(final Object key, final Object value) {
        requiredNotClosed();

        cache.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends Object, ? extends Object> map) {
        requiredNotClosed();

        cache.putAll(map);
    }

    @Override
    public boolean remove(final Object key) {
        final boolean contains = containsKey(key);

        cache.remove(key);

        return contains;
    }

    @Override
    public boolean remove(final Object key, final Object oldValue) {
        if (containsKey(key) && Objects.equals(get(key), oldValue)) {
            cache.remove(key);

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void removeAll(final Set<? extends Object> keys) {
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
