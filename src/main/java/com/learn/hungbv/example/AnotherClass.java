package com.learn.hungbv.example;

import com.learn.hungbv.annotation.Injected;
import com.learn.hungbv.annotation.MultiInstance;

@MultiInstance
public class AnotherClass {
    @Injected
    private MyDependency dependency;

    public MyDependency getDependency() {
        return dependency;
    }
    public void performAction() {
        System.out.println("Performing action...");
    }
}