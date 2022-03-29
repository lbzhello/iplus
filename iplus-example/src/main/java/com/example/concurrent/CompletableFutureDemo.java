package com.example.concurrent;

import xyz.liujin.iplus.util.LogUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureDemo {
    private static ExecutorService pool = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> "hello world", pool)
                // 异步运行
                .thenApplyAsync(it -> {
                    LogUtil.debug(it, "thenApplyAsync");
                    return it;
                }, pool)
                .thenAccept(it -> {
                    LogUtil.debug(it, "thenAccept");
                });
        LogUtil.debug("application end", "main");
    }
}
