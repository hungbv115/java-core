package com.learn.hungbv.example.impl;


import com.learn.hungbv.core.RedisService;
import redis.clients.jedis.Jedis;

public class RedisServiceImpl implements RedisService {

    private final Jedis jedis;

    public RedisServiceImpl() {
        this.jedis = new Jedis("localhost", 6379); // Kết nối Redis tại localhost
    }

    public String getFromRedis(String key) {
        return jedis.lpop(key); // Lấy dữ liệu từ Redis theo key
    }

    public void close() {
        jedis.close(); // Đóng kết nối Redis
    }
}

