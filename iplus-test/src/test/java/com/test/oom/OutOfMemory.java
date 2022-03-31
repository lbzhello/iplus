package com.test.oom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OutOfMemory {
    private static final Logger logger = LoggerFactory.getLogger(OutOfMemory.class);

    List<Integer[]> localCache = new ArrayList<>();

    public String foo(int count) {
        for (int i = 0; i < count; i++) {
            localCache.add(new Integer[1024 * 1024]); // 1m
        }
        return "success";
    }
}
