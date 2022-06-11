package com.test.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;
import xyz.liujin.iplus.util.math.MathUtil;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;

public class OperatorTest {

    /**
     * 阻塞运行
     */
    @Test
    public void blockTest() {
        Integer block = Flux.range(1, 5)
                .subscribeOn(Schedulers.newElastic("subs-pool"))
                .reduce(Math::addExact)
                .block();

        LogUtil.debug(block);
    }

    @Test
    public void delayElementsTest() {
        Flux.range(1, 5)
                // 每 1 秒发射一个元素
                .delayElements(Duration.ofSeconds(1))
                .subscribe(it -> {
                    LogUtil.debug(it);
                });

        DebugUtil.sleep(10000);
    }

    @Test
    public void collectList() {
        Flux.range(1, 10)
                // 收集元素，组成列表
                .collectList()
                .subscribe(it -> LogUtil.debug(it));
    }

    @Test
    public void reduceTest() {
        Flux.range(1, 10)
                // 合计
                .reduce(Math::addExact)
                .subscribe(it -> LogUtil.debug(it));
    }

    @Test
    public void toFluxTest() {
        Mono.just(1)
                // Mono 转 Flux
                .flux()
                .subscribe(it -> LogUtil.debug(it));
    }

    /**
     * 转成热数据流，cache 前的操作会被计算一次并缓存；
     * 后续从缓存的数据流发射数据
     */
    @Test
    public void cacheTest() {
        Flux<Integer> cachedFlux = Flux.range(1, 3)
                .map(it -> {
                    // 生成 100 以内的随机数
                    int random = MathUtil.randomInt(100);
                    LogUtil.debug(random, "random");

                    return random;
                })
                .cache();

        // 第一次消费时 cache 开始计算并缓存
        cachedFlux.subscribe(it -> {
            LogUtil.debug(it, "subscriber-a");
        });

        // 后续从缓存中消费，和第一次收到的元素一致
        cachedFlux.subscribe(it -> {
            LogUtil.debug(it, "subscriber-b");
        });

    }

    /**
     * 窗口分批
     */
    @Test
    public void windowTest() {
        Flux.range(1, 10)
                // 每收集三个元素后，向下游发射一个事件
                .window(3)
                .flatMap(Flux::collectList)
                .subscribe(it -> LogUtil.debug(it));
    }

    /**
     * 连接
     * 前面的流发射完毕后，接着发射后面的流
     */
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
     * 前面的流发射完毕后，接着发射后面的流
     */
    @Test
    public void concatTest() {
        Flux.concat(Flux.just(1, 2, 3), Flux.just(4, 5, 6))
                .subscribe(it -> {
                    LogUtil.debug(it);
                });
    }

    /**
     * 将多个流合并成一个流
     * 各个流发射元素互补影响
     */
    @Test
    public void mergeTest() {
        // 每 100 毫秒发射一个元素 "A"
        Flux<String> fluxA = Flux.interval(Duration.ofMillis(100)).map(it -> "A");
        // 每 150 毫秒发射一个元素 "B"
        Flux<String> fluxB = Flux.interval(Duration.ofMillis(150)).map(it -> "B");

        Flux.merge(fluxA, fluxB)
                .subscribe(it -> LogUtil.debug(it));

        DebugUtil.sleep(1000);
    }

    /**
     * zip 异步流压缩
     */
    @Test
    public void zipAsyncTest() {
        Flux<Integer> intFlux = Flux.just(1, 2, 3, 4).delayElements(Duration.ofMillis(100))
                .doOnNext(it -> LogUtil.debug(it, "intFlux-doOnNext"));

        Flux<String> strFlux = Flux.just("a", "b", "c").delayElements(Duration.ofMillis(200))
                .doOnNext(it -> LogUtil.debug(it, "strFlux-doOnNext"));

        // 发射 [1, "a"], [2, "b"], [3, "C"]
        Flux.zip(intFlux, strFlux)
                .publishOn(Schedulers.newElastic("zip-pool"))
                .subscribe(it -> LogUtil.debug(it, "zip-subscribe"));

        DebugUtil.sleep(1000);
    }

    /**
     * 压缩，从每个流取出一个元素，结合成元祖/数组发射给下游；
     * 其中一个流发射 onComplete 事件则结束，多余的事件不在发出；
     */
    @Test
    public void zipTest() {
        // 发射 [1, "a"], [2, "b"], [3, "C"]
        Flux.zip(Flux.just(1, 2, 3, 4), Flux.just("a", "b", "c"))
                .subscribe(it -> {
                    LogUtil.debug(it);
                });
    }

    @Test
    public void groupByTest() {
        Flux.range(1, 10)
                // 偶数分一组，基数分一组
                .groupBy(it -> it % 2 == 0)
                .flatMap(Flux::collectList)
                .subscribe(it -> LogUtil.debug(it));
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
            int i = MathUtil.randomInt(100);
            DebugUtil.sleep(i);

            sink.next("user-" + id);
            sink.complete();
        }).subscribeOn(Schedulers.newElastic("user-pool"));
    }

    @Test
    public void flatMapTest() {
        Flux.range(1, 5)
                // 元素映射成流
                .flatMap(it -> Flux.just(it, it))
                .subscribe(it -> LogUtil.debug(it));
    }

    @Test
    public void doOnNextTest() {
        Flux.range(1, 2)
                // 每个元素打印一次
                .doOnNext(it -> LogUtil.debug(it, "doOnNext"))
                .subscribe(it -> LogUtil.debug(it, "subscribe"));
    }

    @Test
    public void filterTest() {
        Flux.range(1, 5)
                // 过滤出偶数
                .filter(it -> it % 2 == 0)
                .subscribe(it -> LogUtil.debug(it));
    }

    @Test
    public void mapTest() {
        Flux.range(1, 2)
                // 放大两倍
                .map(it -> it * 2)
                .subscribe(it -> LogUtil.debug(it));
    }

    /**
     * 操作符融合
     */
    @Test
    public void fusionTest() {
        Function<Integer, Integer> f1 = x -> x + 1;
        Function<Integer, Integer> f2 = x -> x * 2;

        Flux.just(1, 2, 3)
                .map(f1)
                .map(f2)
                .subscribe();

        // 可以被优化成
        Function<Integer, Integer> f3 = x -> f2.apply(f1.apply(x));
        Flux.just(1, 2, 3)
                .map(f3)
                .subscribe();
    }
}
