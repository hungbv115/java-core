package com.learn.hungbv.example;

import com.learn.hungbv.annotation.RedisListener;
import com.learn.hungbv.annotation.Singleton;

@Singleton
public class MyDependency {

    @RedisListener(key = "messageQueue")
    public void doSomething(String data) {
        System.out.println("Processing data: " + data);
    }
}