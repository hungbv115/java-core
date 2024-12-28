package com.learn.hungbv.example;

import com.learn.hungbv.annotation.Singleton;

@Singleton
public class MyDependency {
    public void doSomething() {
        System.out.println("MyDependency is doing something");
    }
}