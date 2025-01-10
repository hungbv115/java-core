package com.learn.hungbv.example.impl;

import com.learn.hungbv.annotation.Schedule;
import com.learn.hungbv.annotation.Singleton;
import com.learn.hungbv.example.MyInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class MyInterface1Impl implements MyInterface {

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
}
