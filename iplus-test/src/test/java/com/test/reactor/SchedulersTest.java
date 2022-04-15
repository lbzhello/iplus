package com.test.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.debug.DebugUtil;

import java.util.concurrent.Executors;

public class SchedulersTest {
    /**
     * 线程调度示例
     */
    @Test
    public void schedulersTest() {
        Flux.just(1, 2, 3)
                .map(i -> i + 1)
                // 切换下一个线程池
                .publishOn(Schedulers.newElastic("publish-pool"))
                .map(i -> i * 2)
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
