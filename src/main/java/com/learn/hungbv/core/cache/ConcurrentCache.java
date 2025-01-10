package com.learn.hungbv.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//khi cần cache thread-safe nhưng không có LRU hoặc TTL.
public class ConcurrentCache<K, V> implements Cache<K, V>{

    private final Map<K, CacheItem<V>> cache;

    public ConcurrentCache(int maxSize) {
        this.cache = new ConcurrentHashMap<>(maxSize);
    }

    public ConcurrentCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public V get(K key) {
        CacheItem<V> getCacheItem = cache.get(key);
        if (getCacheItem == null) {
            return null;
        }
        return getCacheItem.getValue();
    }

    @Override
    public void put(K key, V value) {
        CacheItem<V> setCacheItem = new CacheItem<>(value);
        cache.put(key, setCacheItem);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
