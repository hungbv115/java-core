package com.learn.hungbv.example;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

// ví dụ đồng thời (parallel)
public class ForkJoinPoolExample {

    public static void main(String[] args) {
        // Dãy số cần tính tổng
        int[] numbers = new int[1000];
        for (int i = 0; i < 1000; i++) {
            numbers[i] = i + 1;
        }

        // Tạo ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // Tạo một tác vụ để tính tổng
        SumTask sumTask = new SumTask(numbers, 0, numbers.length);

        // Gửi tác vụ vào ForkJoinPool và nhận kết quả
        int result = forkJoinPool.invoke(sumTask);

        // In kết quả
        System.out.println("Total sum is: " + result);
    }

    // Định nghĩa một tác vụ tính tổng sử dụng RecursiveTask
    static class SumTask extends RecursiveTask<Integer> {
        private final int[] numbers;
        private final int start;
        private final int end;

        // Constructor
        public SumTask(int[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            // Nếu dãy có ít hơn 10 phần tử, tính tổng trực tiếp
            if (end - start <= 10) {
                int sum = 0;
                for (int i = start; i < end; i++) {
                    sum += numbers[i];
                }
                return sum;
            } else {
                // Chia tác vụ thành 2 phần con
                int mid = (start + end) / 2;
                SumTask leftTask = new SumTask(numbers, start, mid);
                SumTask rightTask = new SumTask(numbers, mid, end);

                // Fork (chia) tác vụ
                leftTask.fork();
                rightTask.fork();

                // Join (kết hợp) kết quả
                int leftResult = leftTask.join();
                int rightResult = rightTask.join();

                return leftResult + rightResult;
            }
        }
    }
}
