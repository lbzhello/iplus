package com.test.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;

import java.util.concurrent.Executors;

public class SchedulersTest {
    /**
     * 线程调度示例
     */
    @Test
    public void schedulersTest() {
        Flux.just(1)
                .doOnNext(it -> LogUtil.debug(1))
                // 弹性线程池
                .publishOn(Schedulers.newElastic("newElastic"))
                .doOnNext(it -> LogUtil.debug(2))
                // 固定数量线程池
                .publishOn(Schedulers.newParallel("newParallel", 10))
                .doOnNext(it -> LogUtil.debug(3))
                // 单线程池
                .publishOn(Schedulers.newSingle("newSingle"))
                .doOnNext(it -> LogUtil.debug(4))
                // 包装 Executor
                .publishOn(Schedulers.fromExecutor(Executors.newCachedThreadPool()))
                .doOnNext(it -> LogUtil.debug(5))
                // 配置订阅线程池（默认线程池）
                .subscribeOn(Schedulers.newElastic("subscribe-pool"))
                .subscribe(i -> System.out.println(i));

        DebugUtil.sleep(100);
    }

    @Test
    public void ExecutorTest() {
        Executors.newSingleThreadExecutor();
        Executors.newCachedThreadPool();
    }
}
