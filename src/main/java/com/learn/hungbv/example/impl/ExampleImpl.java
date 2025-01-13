package com.learn.hungbv.example.impl;

import com.learn.hungbv.annotation.Schedule;
import com.learn.hungbv.annotation.Singleton;
import com.learn.hungbv.example.MyInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Singleton
public class ExampleImpl implements MyInterface {

//    @Schedule(time = 1)
    @Override
    public void performAction() {
        List<String> A = List.of("abc", "def", "ghi");
        List<String> B = List.of("abc123", "def456", "ghi789", "xyz000", "abcxyz", "abc1s23", "uef456");

        // Tạo HashMap với key là phần tử của A và value là danh sách các phần tử của B
        Map<String, List<String>> result = B.stream()
                .filter(b -> A.stream().anyMatch(b::startsWith))  // Lọc các phần tử của B bắt đầu bằng một phần tử trong A
                .collect(Collectors.groupingBy(b -> A.stream()
                        .filter(b::startsWith)
                        .findFirst()
                        .orElse(""))); // Tìm phần tử đầu tiên trong A mà B bắt đầu với nó làm key

        // In kết quả
        System.out.println(result);
    }

    @Schedule(time = 1)
    public void example() {
        int[] input = {3,2,4};
        int target = 6;
        int[] output = new int[2];
        for (int i = 0; i < input.length; i++) {
            for (int j = i + 1; j < input.length; j++) {
                if (input[i] + input[j] == target) {
                    output[0] = i;
                    output[1] = j;
                    System.out.println(Arrays.toString(output));
                    break;
                }
            }
        }
    }

//    @Schedule(time = 1)
    public void roundRobin() {
        int[] data = new int[1000];
        for (int i = 0; i < 1000; i++) {
            data[i] = i + 1;
        }

        int numThreads = 4;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Runnable> tasks = new ArrayList<>();

        // Phân phối dữ liệu theo Round Robin
        for (int i = 0; i < data.length; i++) {
            final int element = data[i];
            tasks.add(() -> System.out.println(Thread.currentThread().getName() + " xử lý phần tử: " + element));
        }

        // Chạy các tác vụ theo Round Robin
        for (int i = 0; i < tasks.size(); i++) {
            executor.submit(tasks.get(i));
        }

        executor.shutdown();
    }

//    @Schedule(time = 1)
    public void queueBase() throws InterruptedException {
        int[] data = new int[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Đưa dữ liệu vào queue
        for (int i = 0; i < data.length; i++) {
            queue.put(data[i]);
        }

        // Tạo và chạy các luồng xử lý
        for (int i = 0; i < 4; i++) {
            executor.submit(() -> {
                try {
                    while (!queue.isEmpty()) {
                        Integer element = queue.take();
                        System.out.println(Thread.currentThread().getName() + " xử lý phần tử: " + element);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        executor.shutdown();
    }

//    @Schedule(time = 1)
    public void workStealing() {
        int[] data = new int[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        // Các luồng "rảnh" sẽ "trộm" công việc từ các luồng bận rộn hơn
        ExecutorService executorService = Executors.newWorkStealingPool();

        for (int i = 0; i < data.length; i++) {
            int element = data[i];
            executorService.submit(() -> System.out.println(Thread.currentThread().getName() + " Lam viec voi du du lieu: " + element));
        }

        executorService.shutdown();
    }

//    @Schedule(time = 1)
    public void fixedPartition() {
        int[] data = new int[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        int numberThreads = 4;
        int partitionSize = data.length/numberThreads;

        ExecutorService executorService = Executors.newFixedThreadPool(numberThreads);
        for (int i = 0; i < 4; i++) {
            int start = i * partitionSize;
            int end = (i + 1) * partitionSize;
            if (i == numberThreads - 1) {
                end = data.length;
            }
            executorService.submit(new Worker(start, end, data));
        }
        executorService.shutdown();
    }

    static class Worker implements Runnable{
        private final int start;
        private final int end;
        private final int[] data;

        public Worker(int start, int end, int[] data) {
            this.start = start;
            this.end = end;
            this.data = data;
        }

        @Override
        public void run() {
            for(int i = start; i < end; i++) {
                System.out.println(Thread.currentThread().getName() + " Lam viec voi du du lieu: " + data[i]);
            }
        }
    }
}
