package com.learn.hungbv.example;

import com.learn.hungbv.annotation.Injected;
import com.learn.hungbv.annotation.MultiInstance;
import com.learn.hungbv.annotation.Schedule;
import com.learn.hungbv.core.cache.Cache;
import com.learn.hungbv.core.cache.LinkedCache;

@MultiInstance
public class AnotherClass {
    @Injected
    private MyDependency dependency;

    public MyDependency getDependency() {
        return dependency;
    }

//    @Schedule(time = 1)
    public void performAction() throws InterruptedException {
        Cache<String, String> cache = new LinkedCache<>(10, 2000);
        cache.put("hungbv", "hung");

        Thread.sleep(6000);
        String iyt = cache.get("hungbv");
        System.out.println(iyt);
    }
}