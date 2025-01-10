package com.learn.hungbv.core;

public interface RedisService {
    String getFromRedis(String key);
    void close();
}
