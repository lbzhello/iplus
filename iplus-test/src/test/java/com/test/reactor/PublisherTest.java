package com.test.reactor;

import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.scheduling.annotation.Schedules;
import reactor.core.publisher.EmitterProcessor;
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

    }

    @Test
    public void processorTest() {
        EmitterProcessor<Object> emitterProcessor = EmitterProcessor.create();
        emitterProcessor.publishOn(Schedulers.newElastic("newThread")).map(it -> {
            LogUtil.debug("map");
            return it;
        }).onErrorContinue((e, it) -> {
            System.out.println(it);
        }).subscribe(it -> {
            LogUtil.debug("subscribe");
            System.out.println(it);
        }, e -> {
            LogUtil.debug("onError");
            System.out.println(e.getMessage());
        });

        emitterProcessor.onNext(1);
        emitterProcessor.onNext(1);
        emitterProcessor.onNext(1);
        emitterProcessor.onComplete();

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

        // 延迟创建发布者（订阅时创建），由 from 包装，不支持异步
        Flux.defer(() -> Flux.just(1, 2, 3))
                .subscribe(it -> LogUtil.debug(it, "defer"));

        // 定时发送事件
        Flux.interval(Duration.ofSeconds(1))
                .subscribe(it -> LogUtil.debug(it, "interval"));

        DebugUtil.sleep(200);
    }

    /**
     * 延迟创建发布者（订阅时创建）
     */
    @Test
    public void deferTest() {
        Flux<Integer> deferFlux = Flux.defer(() -> {
            return Flux.from(new Publisher<Integer>() {
                {
                    // 不会打印，因为延迟创建
                    LogUtil.debug("create publisher", "defer");
                }

                @Override
                public void subscribe(Subscriber<? super Integer> s) {
                    s.onNext(1);
                    s.onNext(2);
                    s.onComplete();
                }
            });
        });

        Flux<Object> createFlux = Flux.from(new Publisher<Integer>() {
            {
                // 直接打印
                LogUtil.debug("create publisher", "from");
            }

            @Override
            public void subscribe(Subscriber<? super Integer> s) {
                s.onNext(1);
                s.onNext(2);
                s.onComplete();
            }
        });

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
