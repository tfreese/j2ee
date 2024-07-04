// Created: 04 Juli 2024
package de.freese.jpa.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;

/**
 * @author Thomas Freese
 */
public class JCacheCaffeine implements Cache {
    @Override
    public void clear() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean containsKey(final Object o) {
        return false;
    }

    @Override
    public void deregisterCacheEntryListener(final CacheEntryListenerConfiguration cacheEntryListenerConfiguration) {

    }

    @Override
    public Object get(final Object o) {
        return null;
    }

    @Override
    public Map getAll(final Set set) {
        return Map.of();
    }

    @Override
    public Object getAndPut(final Object o, final Object o2) {
        return null;
    }

    @Override
    public Object getAndRemove(final Object o) {
        return null;
    }

    @Override
    public Object getAndReplace(final Object o, final Object o2) {
        return null;
    }

    @Override
    public CacheManager getCacheManager() {
        return null;
    }

    @Override
    public Configuration getConfiguration(final Class aClass) {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Object invoke(final Object o, final EntryProcessor entryProcessor, final Object... objects) throws EntryProcessorException {
        return null;
    }

    @Override
    public Map invokeAll(final Set set, final EntryProcessor entryProcessor, final Object... objects) {
        return Map.of();
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public Iterator<Entry> iterator() {
        return null;
    }

    @Override
    public void loadAll(final Set set, final boolean b, final CompletionListener completionListener) {

    }

    @Override
    public void put(final Object o, final Object o2) {

    }

    @Override
    public void putAll(final Map map) {

    }

    @Override
    public boolean putIfAbsent(final Object o, final Object o2) {
        return false;
    }

    @Override
    public void registerCacheEntryListener(final CacheEntryListenerConfiguration cacheEntryListenerConfiguration) {

    }

    @Override
    public boolean remove(final Object o) {
        return false;
    }

    @Override
    public boolean remove(final Object o, final Object o2) {
        return false;
    }

    @Override
    public void removeAll(final Set set) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public boolean replace(final Object o, final Object o2, final Object v1) {
        return false;
    }

    @Override
    public boolean replace(final Object o, final Object o2) {
        return false;
    }

    @Override
    public Object unwrap(final Class aClass) {
        return null;
    }
}
