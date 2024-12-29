package com.learn.hungbv.core;

import com.learn.hungbv.annotation.BootstrapApplication;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApplicationContext {
    private static final AtomicBoolean running = new AtomicBoolean(true);
    public static void run(Class<?> mainClass) {
        if (!mainClass.isAnnotationPresent(BootstrapApplication.class)) {
            throw new IllegalArgumentException("Lớp khởi động phải được chú thích bằng @BootstrapApplication");
        }

        BootstrapApplication annotation = mainClass.getAnnotation(BootstrapApplication.class);
        String basePackage = annotation.basePackage().isEmpty() ?
                mainClass.getPackageName() : annotation.basePackage();

        System.out.println("Bắt đầu ứng dựng, quét package: " + basePackage);

        try {
            InstanceManager.initializeInstances(basePackage);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Lỗi khi tải class", e);
        }

        // Khởi động Scheduler
        Scheduler.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Đang thực hiện dừng ứng dụng...");
            Scheduler.stop();
            running.set(false);
        }));

        // Vòng lặp chính giữ ứng dụng chạy
//        while (running.get()) {
//            try {
//                Thread.sleep(1000); // Chờ 1 giây trước khi kiểm tra lại
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                System.out.println("Ứng dụng bị gián đoạn.");
//                break;
//            }
//        }

        System.out.println("Ứng dụng đã dừng.");
    }
}

