package com.learn.hungbv.core;

import com.learn.hungbv.annotation.RedisListener;

import java.lang.reflect.Method;
import java.util.Objects;

public class RedisListenerProcessor {

    private final RedisService redisService;

    public RedisListenerProcessor(RedisService redisService) {
        this.redisService = redisService;
    }

    public void process(Class<?> clazz) throws Exception {
        // Lặp qua tất cả các method trong class
        for (Method method : clazz.getDeclaredMethods()) {
            // Kiểm tra nếu phương thức có annotation @RedisListener
            if (method.isAnnotationPresent(RedisListener.class)) {
                RedisListener redisListener = method.getAnnotation(RedisListener.class);
                String key = redisListener.key();

                // Lấy dữ liệu từ Redis
                String data = redisService.getFromRedis(key);
                if(Objects.isNull(data)) continue;

                // Gọi phương thức và truyền dữ liệu vào
                method.setAccessible(true);
                method.invoke(clazz.getDeclaredConstructor().newInstance(), data);
            }
        }
    }
}

