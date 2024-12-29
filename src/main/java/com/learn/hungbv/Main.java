package com.learn.hungbv;

import com.learn.hungbv.annotation.BootstrapApplication;
import com.learn.hungbv.core.ApplicationContext;
import com.learn.hungbv.core.InstanceManager;
import com.learn.hungbv.example.AnotherClass;
import com.learn.hungbv.example.MyInterface;
import com.learn.hungbv.example.MySingleton;

import java.util.List;

@BootstrapApplication
public class Main {
    public static void main(String[] args) {
//        MySingleton instance1 = InstanceManager.getInstance(MySingleton.class);
//        MySingleton instance2 = InstanceManager.getInstance(MySingleton.class);
//        if (instance1 != null && instance2 != null) {
//            System.out.println(instance1 == instance2);  // Should print true
//            instance1.doSomething();
//        } else {
//            System.out.println("Failed to create MySingletonClass instances");
//        }
//        instance1.getDependency().doSomething();
//
//        AnotherClass instance3 = InstanceManager.getInstance(AnotherClass.class);
//        AnotherClass instance4 = InstanceManager.getInstance(AnotherClass.class);
//
//        if (instance3 != null && instance4 != null) {
//            System.out.println(instance3 != instance4);  // Should print true (different instances)
//            instance3.performAction();
//            instance4.performAction();
//        } else {
//            System.out.println("Failed to create AnotherClass instances");
//        }

//        // Đăng ký interface và lớp triển khai
//        InstanceManager.registerImplementation(MyInterface.class, MyInterfaceImpl.class);

        // Sử dụng đối tượng MyInterface
//        List<MyInterface> myInterface = InstanceManager.getInstances(MyInterface.class);
//        myInterface.forEach(MyInterface::performAction);

        // Khởi động ứng dụng
        ApplicationContext.run(Main.class);
    }
}