package com.test.stream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {
    private static final Logger logger = LoggerFactory.getLogger(StreamTest.class);

    /**
     * parallel 是异步的，但是 forEach 会同步等待结果
     */
    @Test
    public void parallelTest() {
        logger.debug("start");
        Stream.of(1, 2, 3, 4)
                .parallel()
                .forEach(it -> {
                    DebugUtil.sleep(2000);
                    logger.debug("resp " + it);
                });
        logger.debug("end");
    }

    /**
     * stream 消费之后不可重用
     */
    @Test
    public void streamReuseTest() {
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4);
        integerStream.forEach(it -> logger.debug("first: {}", it));
        integerStream.forEach(it -> logger.debug("second: {}", it));
    }

    @Test
    public void completableFutureTest() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            return "hello world";
        }, Executors.newSingleThreadExecutor()).thenApply(it -> {
            LogUtil.debug("thenApply");
            return it + 1;
        });
    }

    @Test
    public void streamTest() {
        List<Integer> collect = Stream.iterate(1, it -> it * 2).limit(5).collect(Collectors.toList());
        System.out.println();
    }
}
