package com.learn.hungbv.example;

import com.learn.hungbv.annotation.Injected;
import com.learn.hungbv.annotation.Singleton;

@Singleton
public class MySingleton {
    // Private constructor to prevent instantiation
    private MySingleton() {
    }

    public void doSomething() {
        System.out.println("Doing something...");
    }

    @Injected
    private MyDependency dependency;


    public MyDependency getDependency() {
        return dependency;
    }
}
