package com.example.reactor;

import java.util.stream.Stream;

public class FluxDemo {
    public static void main(String[] args) {
        Stream.of(1, 2, 3, 4).filter(i -> i % 2 == 0).map(i -> i * 2).forEach(System.out::println);
    }
}
