package com.learn.hungbv.core;

import com.learn.hungbv.annotation.Injected;
import com.learn.hungbv.annotation.MultiInstance;
import com.learn.hungbv.annotation.Singleton;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class InstanceManager {
    private static final Map<Class<?>, Object> context = new HashMap<>();
    private static final Map<Class<?>, Set<Class<?>>> interfaceImplementations = new HashMap<>();

    static {
        try {
            initializeInstances("com.learn.hungbv.example"); // Thay đổi package này theo dự án của bạn
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize singletons", e);
        }
    }

    public static <T> T getInstance(Class<T> clazz) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            return getSingletonInstance(clazz);
        } else if (clazz.isAnnotationPresent(MultiInstance.class)) {
            return getMultiInstance(clazz);
        } else {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with @Singleton or @MultiInstance");
        }
    }

    public static <T> List<T> getInstances(Class<T> interfaceClass) {
        if (interfaceImplementations.containsKey(interfaceClass)) {
            List<T> instances = new ArrayList<>();
            for(Class<?> element : interfaceImplementations.get(interfaceClass)) {
                instances.add((T) getInstance(element));
            }
            return instances;
        }
        throw new IllegalArgumentException("Interface " + interfaceClass.getName() + " has no registered implementations.");
    }

    private static <T> T getSingletonInstance(Class<T> clazz) {
        if (!context.containsKey(clazz)) {
            synchronized (context) {
                if (!context.containsKey(clazz)) {
                    context.put(clazz, createInstance(clazz));
                }
            }
        }
        return (T) context.get(clazz);
    }

    private static <T> T getMultiInstance(Class<T> clazz) {
        return createInstance(clazz);
    }

    private static void initializeInstances(String packageName) throws ClassNotFoundException, IOException {
        Set<Class<?>> classes = getClasses(packageName);

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Singleton.class) || clazz.isAnnotationPresent(MultiInstance.class)) {
                context.put(clazz, createInstance(clazz));
            }
            for (Class<?> iface : clazz.getInterfaces()) {
                interfaceImplementations.computeIfAbsent(iface, k -> new HashSet<>()).add(clazz);
            }

        }
    }

    private static Set<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        URL resource = classLoader.getResource(path);

        File directory = new File(URLDecoder.decode(resource.getFile(), "UTF-8"));
        if (!directory.exists()) {
            throw new ClassNotFoundException("Directory " + directory.getAbsolutePath() + " not found");
        }

        return new HashSet<>(findClasses(directory, packageName));
    }

    private static Set<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

    private static <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true); // Bypass the private access modifier
            T in = constructor.newInstance();
            injectDependencies(clazz, in);
            return in;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create instance of class " + clazz.getName(), e);
        }
    }

    private static void injectDependencies(Class<?> clazz, Object instance) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Injected.class)) {
                Class<?> fieldType = field.getType();
                Object dependencyInstance = getInstance(fieldType);
                try {
                    field.setAccessible(true);
                    field.set(instance, dependencyInstance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject dependency for field " + field.getName() + " in class " + clazz.getName(), e);
                }
            }
        }
    }

    public static void registerImplementation(Class<?> interfaceClass, Set<Class<?>> implementationClass) {
        if (interfaceClass.isInterface()) {
            interfaceImplementations.put(interfaceClass, implementationClass);
        } else {
            throw new IllegalArgumentException("Class " + interfaceClass.getName() + " is not an interface");
        }
    }
}



