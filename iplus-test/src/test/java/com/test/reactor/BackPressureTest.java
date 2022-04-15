package com.test.reactor;

import io.netty.util.internal.SocketUtils;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.debug.DebugUtil;

import java.util.function.Consumer;

public class BackPressureTest {

    /**
     * 调整背压策略
     */
    @Test
    public void onBackPressureXxxTest() {
        Flux.create(new Consumer<FluxSink<Integer>>() {
            @Override
            public void accept(FluxSink<Integer> fluxSink) {
                Flux.range(1, 10).subscribe(i -> {
                    // 模拟上游处理速度较快
                    DebugUtil.sleep(10);
                    System.out.println(("publish >>> " + i));
                    fluxSink.next(i);
                });

                fluxSink.complete();
            }
        }, FluxSink.OverflowStrategy.DROP)
                .doOnRequest(it -> {
                    // 下游发起 request 数量
                    System.out.println("               request: " + it);
                })
                .onBackpressureBuffer()
                .onBackpressureDrop()
                .onBackpressureLatest()
                .onBackpressureError()
                .publishOn(Schedulers.newElastic("publishOn-test"));
    }

    @Test
    public void backPressureStrategyTest() {
        Flux.create(new Consumer<FluxSink<Integer>>() {
            @Override
            public void accept(FluxSink<Integer> fluxSink) {
                Flux.range(1, 10).subscribe(i -> {
                    // 模拟上游处理速度较快
                    DebugUtil.sleep(10);
                    System.out.println(("publish >>> " + i));
                    fluxSink.next(i);
                });

                fluxSink.complete();
            }
        }, FluxSink.OverflowStrategy.DROP)
                .doOnRequest(it -> {
                    // 下游发起 request 数量
                    System.out.println("               request: " + it);
                })
                // 预取数量，下游向上游请求元素数量，会缓存起来，默认 256，为了测试，设置为1
                .publishOn(Schedulers.newElastic("publishOn-pool"), 1)
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("onSubscribe");

                        // 订阅时请求一个数据
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println("               receive <<< " + value);
                        // 模拟下游处理速度较慢
                        DebugUtil.sleep(30);

                        // 每处理完 1 个，再请求 1 个
                        request(1);
                  }

                    @Override
                    protected void hookOnComplete() {
                        System.out.println("onComplete");
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        System.out.println("onError");
                        throwable.printStackTrace();
                    }
                });

        DebugUtil.sleep(1000);
    }

    /**
     * 消费者速度控制
     */
    @Test
    public void requestTest() {
        Flux.range(1, 100)
                .subscribe(new Subscriber<Integer>() {
                    private Subscription subscription;
                    private int count = 0;
                    private final int BATCH_SIZE = 10; // 消费者处理速度

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(BATCH_SIZE); // 必须调用此方法订阅者才开始处理数据
                    }

                    @Override
                    public void onNext(Integer integer) {
                        count++;
                        System.out.println(integer);
                        if (count >= BATCH_SIZE) {
                            count = 0; // 从新计数
                            System.out.println("request more " + BATCH_SIZE);
                            subscription.request(BATCH_SIZE); // 处理完成，继续请求更多的数据
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }
}
