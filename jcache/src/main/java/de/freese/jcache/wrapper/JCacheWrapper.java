// Created: 08 Feb. 2025
package de.freese.jcache.wrapper;

import javax.cache.Caching;

/**
 * @author Thomas Freese
 */
public final class JCacheWrapper {

    public static void forMap() {
        System.setProperty(Caching.JAVAX_CACHE_CACHING_PROVIDER, "com.hazelcast.cache.HazelcastMemberCachingProvider");
    }

    private JCacheWrapper() {
        super();
    }
}
