package com.test.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;

import java.util.function.Consumer;

public class OperatorTest {

    /**
     * 转成热数据流，cache 前的操作会被计算一次并缓存；
     * 后续从缓存的数据流发射数据
     */
    @Test
    public void cacheTest() {
        Flux<Integer> cachedFlux = Flux.just(1, 2, 3)
                .map(it -> {
                    LogUtil.debug("cached-map");
                    int i = Double.valueOf(Math.random() * 10).intValue();
                    return it * i;
                })
                .cache();

        cachedFlux.subscribe(it -> {
            LogUtil.debug(it, "subs-a");
        });

        cachedFlux.subscribe(it -> {
            LogUtil.debug(it, "subs-b");
        });

    }

    @Test
    public void concatWithTest() {
        Flux.just(1, 2, 3)
                .concatWith(Flux.just(4, 5, 6))
                .subscribe(it -> {
                    LogUtil.debug(it);
                });
    }

    /**
     * 连接
     */
    @Test
    public void concatTest() {
        Flux.concat(Flux.just(1, 2, 3), Flux.just(4, 5, 6))
                .subscribe(it -> {
                    LogUtil.debug(it);
                });
    }

    /**
     * 压缩，一对一合并
     */
    @Test
    public void zipTest() {
        Flux.zip(Flux.just(1, 2, 3), Flux.just("a", "b", "c"))
                .subscribe(it -> {
                    LogUtil.debug(it);
                });
    }

    /**
     * 映射成流
     */
    @Test
    public void flatMapTest2() {
        Flux.range(1, 5)
                .flatMap(id -> findUser(id))
                .publishOn(Schedulers.newElastic("publishOn-pool"))
                .subscribe(user -> {
                    LogUtil.debug(user);
                });

        DebugUtil.sleep(300);
    }

    public static Flux<String> findUser(Integer id) {
        return Flux.create((Consumer<FluxSink<String>>) sink -> {
            // 模拟业务耗时
            int i = Double.valueOf(Math.random() * 100).intValue();
            DebugUtil.sleep(i);

            sink.next("user-" + id);
            sink.complete();
        }).subscribeOn(Schedulers.newElastic("user-pool"));
    }

    @Test
    public void flatMapTest() {
        Flux.range(1, 5)
                .flatMap(it -> Flux.just(it, it))
                .subscribe(it -> LogUtil.debug(it));
    }

    @Test
    public void mapTest() {
        Flux.range(1, 5)
                .map(it -> it * it)
                .subscribe(it -> LogUtil.debug(it));
    }
}
