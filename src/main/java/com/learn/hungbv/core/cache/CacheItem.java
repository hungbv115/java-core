package com.learn.hungbv.core.cache;

import java.time.Instant;

public class CacheItem<T> {
    private final T value;
    private Instant expirationTime;

    public CacheItem(T value, long ttlInMillis) {
        this.value = value;
        this.expirationTime = Instant.now().plusMillis(ttlInMillis); // TTL (Time-To-Live) tính bằng milliseconds
    }

    public CacheItem(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expirationTime);
    }
}

