package com.test.reactor;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PublisherTest {

    public static void main(String[] args) {
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

    /**
     * 发布者创建
     */
    @Test
    public void publishCreateTest() {
        // 空的发布者，不会发布任何元素；只有 onComplete 事件；
        Flux.empty()
                .subscribe(it -> LogUtil.debug(it, "empty"));

        // 根据参数列表生成事件流
        Flux.just(1, 2, 3)
                .subscribe(it -> LogUtil.debug(it, "just"));

        // 数字序列事件流
        Flux.range(1, 3)
                .subscribe(it -> LogUtil.debug(it, "range"));

        // 根据可迭代（Iterable）集合生成
        Flux.fromIterable(List.of(1, 2, 3))
                .subscribe(it -> LogUtil.debug(it, "fromIterable"));

        // 根据数组生成事件流
        Flux.fromArray(new Integer[]{1, 2, 3})
                .subscribe(it -> LogUtil.debug(it, "fromArray"));

        Flux.defer(() -> Flux.just(1, 2, 3))
                .subscribe(it -> LogUtil.debug(it, "defer"));

        // 定时发送事件
        Flux.interval(Duration.ofSeconds(1))
                .subscribe(it -> LogUtil.debug(it, "interval"));

        DebugUtil.sleep(200);
    }

    @Test
    public void generateTest() {
        Flux.generate((Consumer<SynchronousSink<Integer>>) sink -> {
            sink.next(1);
            sink.complete();
        })
                .subscribe(it -> {
                    LogUtil.debug(it, "subscribe");
                });
    }

    /**
     * create 背压
     */
    @Test
    public void createWithBackPressureTest() {
        Flux.create((Consumer<FluxSink<Integer>>) fluxSink -> {
            fluxSink.next(1);
            fluxSink.next(2);
            fluxSink.next(3);
            fluxSink.complete();
        }, FluxSink.OverflowStrategy.DROP)
                .subscribeOn(Schedulers.newElastic("subscribe-pool"))
                .subscribe(it -> {
                    LogUtil.debug(it, "subscribe");
                });

        DebugUtil.sleep(200);
    }

    @Test
    public void createTest() {
        Flux.create((Consumer<FluxSink<Integer>>) fluxSink -> {
            fluxSink.next(1);
            fluxSink.next(2);
            fluxSink.complete();
        })
                .map(it -> {
                    LogUtil.debug(it, "map"); // 运行在 subscribeOn-pool
                    return it * 2;
                })
                .publishOn(Schedulers.newElastic("publishOn-pool"))
                .filter(it -> {
                    LogUtil.debug(it, "filter"); // 运行在 publishOn-pool
                    return it % 2 == 0;
                })
                .subscribeOn(Schedulers.newElastic("subscribeOn-pool"))
                .subscribe(it -> LogUtil.debug(it, "subscribe")); // 运行在 publishOn-pool

        DebugUtil.sleep(200);
    }

    @Test
    public void fromTest() {
        Flux.from((Publisher<Integer>) s -> {
            s.onNext(1);
            s.onNext(2);
            s.onComplete(); // onComplete 不要忘记
        })
//                .subscribeOn(Schedulers.newElastic("subscribeOn-pool"))
                .subscribe(it -> {
                    LogUtil.debug(it);
                });

        DebugUtil.sleep(100);
    }

    /**
     * from 不支持背压
     * from 不支持异步流（ publishOn 切换线程池）
     */
    @Test
    public void fromPublishOnTest() {
        Flux.from((Publisher<Integer>) s -> {
            s.onNext(0);
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onComplete();
        })
                .map(it -> {
                    LogUtil.debug(it, "map");
                    return it * 2;
                })
//                .publishOn(Schedulers.newElastic("publishOn-pool"))
                .filter(it -> {
                    LogUtil.debug(it, "filter");
                    return it % 2 == 0;
                })
                .subscribeOn(Schedulers.newElastic("subscribeOn-pool"))
                .subscribe(it -> LogUtil.debug(it, "subscribe"));

        DebugUtil.sleep(200);
    }

    @Test
    public void just() {
        Flux.just("hello world")
                .subscribe(it -> LogUtil.debug(it));
    }
}
