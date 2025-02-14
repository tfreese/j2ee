// Created: 05 Juli 2024
package de.freese.jcache.spi;

import java.util.Map;
import java.util.Objects;

import javax.cache.Cache;

/**
 * @author Thomas Freese
 */
public record CacheEntry<K, V>(K key, V value) implements Cache.Entry<K, V> {
    public static <A, B> Cache.Entry<A, B> of(final Map.Entry<A, B> entry) {
        return new CacheEntry<>(entry.getKey(), entry.getValue());
    }

    public CacheEntry {
        Objects.requireNonNull(key, "key required");
    }

    @Override
    public K getKey() {
        return key();
    }

    @Override
    public V getValue() {
        return value();
    }

    @Override
    public <T> T unwrap(final Class<T> clazz) {
        return null;
    }
}
