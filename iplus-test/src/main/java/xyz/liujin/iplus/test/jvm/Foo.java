package xyz.liujin.iplus.test.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Foo {
    private static final Logger logger = LoggerFactory.getLogger(Foo.class);

    List<Integer[]> localCache = new ArrayList<>();

    // -Xms32m -Xmx32m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=G:/tmp
    public String foo(int count) {
        for (int i = 0; i < count; i++) {
            localCache.add(new Integer[1024 * 1024]); // 1m
        }
        return "success";
    }
}
