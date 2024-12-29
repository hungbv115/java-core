package com.learn.hungbv.core;

import com.learn.hungbv.annotation.Schedule;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    public static void start() {
        // Lấy tất cả các class đã được quét bởi InstanceManager
        for (Class<?> clazz : InstanceManager.getAllLoadedClasses()) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Schedule.class)) {
                    Schedule schedule = method.getAnnotation(Schedule.class);

                    // Tạo instance nếu cần
                    Object instance = InstanceManager.getInstance(clazz);

                    // Chạy phương thức định kỳ
                    if(schedule.fixedRate() != 0) {
                        executor.scheduleAtFixedRate(() -> {
                            try {
                                method.setAccessible(true);
                                method.invoke(instance);
                            } catch (Exception e) {
                                System.err.println("Lỗi khi chạy phương thức đánh dấu lịch trình: " + method.getName());
                                e.printStackTrace();
                            }
                        }, 0, schedule.fixedRate(), TimeUnit.MILLISECONDS);
                    } else if(schedule.time() != 0) {
                        for(int i = 1; i <= schedule.time(); i++) {
                            try {
                                method.setAccessible(true);
                                method.invoke(instance);
                            } catch (Exception e) {
                                System.err.println("Lỗi khi chạy phương thức đánh dấu lịch trình: " + method.getName());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}

