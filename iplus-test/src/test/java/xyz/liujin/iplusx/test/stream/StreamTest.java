package xyz.liujin.iplusx.test.stream;

import org.junit.jupiter.api.Test;
import xyz.liujin.iplusx.test.TestHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void completableFutureTest() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            return "hello world";
        }, Executors.newSingleThreadExecutor()).thenApply(it -> {
            TestHelper.printCurrentThread("thenApply");
            return it + 1;
        });
    }

    @Test
    public void streamTest() {
        List<Integer> collect = Stream.iterate(1, it -> it * 2).limit(5).collect(Collectors.toList());
        System.out.println();
    }
}
