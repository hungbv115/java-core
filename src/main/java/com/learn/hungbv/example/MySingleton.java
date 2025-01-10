package com.learn.hungbv.example;

import com.learn.hungbv.annotation.Injected;
import com.learn.hungbv.annotation.Schedule;
import com.learn.hungbv.annotation.Singleton;
import com.learn.hungbv.core.RedisListenerProcessor;
import com.learn.hungbv.core.RedisService;
import com.learn.hungbv.example.impl.RedisServiceImpl;

@Singleton
public class MySingleton {

    @Schedule(fixedRate = 2000)
    public void doSomething() throws Exception {
        RedisService redisService = new RedisServiceImpl();

        // Tạo RedisListenerProcessor để xử lý các phương thức được đánh dấu
        RedisListenerProcessor processor = new RedisListenerProcessor(redisService);
        processor.process(MyDependency.class);
        redisService.close();
    }
}
