package com.test.reactor;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;

import java.util.function.Consumer;

/**
 * HIK所有，<br/>
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。<br/>
 *
 * @describtion
 * @copyright Copyright: 2015-2020
 * @creator liubaozhu
 * @create-time 14:08 2022/3/30
 * @department 安检业务部
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user :{修改人} :{修改时间}
 * @modify by reason :{原因}
 **/
public class PublishOnSubscribeOnTest {
    /**
     * publishOn subscribeOn 结合使用
     */
    @Test
    public void pubSubOnTest() {
        Flux.just(1, 2)
                .filter(it -> {
                    LogUtil.debug(it, "filter");
                    return it % 2 == 0;
                })
                .publishOn(Schedulers.newElastic("publishOn-pool-a"))
                .map(it -> {
                    LogUtil.debug(it, "map-a"); // 运行在 publishOn-pool-a
                    return it * 2;
                })
                .publishOn(Schedulers.newElastic("publishOn-pool-b"))
                .map(it -> {
                    LogUtil.debug(it, "map-b"); // 运行在 publishOn-pool-b
                    return it * 2;
                })
                .subscribeOn(Schedulers.newElastic("subscribeOn-pool"))
                .subscribe(it -> {
                    LogUtil.debug(it, "subscribe"); // 运行在 publishOn-pool-b
                });

        DebugUtil.waitMillis(100);
    }

    /**
     * publishOn 多次调用
     */
    @Test
    public void publishOnTest() {
        Flux.just(1, 2)
                .publishOn(Schedulers.newElastic("publishOn-pool-a"))
                .filter(it -> {
                    LogUtil.debug(it, "filter"); // 运行在 publishOn-pool-a
                    return it % 2 == 0;
                })
                .publishOn(Schedulers.newElastic("publishOn-pool-b"))
                .map(it -> {
                    LogUtil.debug(it, "map"); // 运行在 publishOn-pool-b
                    return it * 2;
                })
                .subscribe(it -> {
                    LogUtil.debug(it, "subscribe"); // 运行在 publishOn-pool-b
                });

        DebugUtil.waitMillis(100);
    }

    /**
     * subscribeOn 多次调用
     */
    @Test
    public void subscribeOnTest2() {
        Flux.just(1, 2)
                .doOnSubscribe(s -> LogUtil.debug("doOnSubscribe-b")) // 运行在subscribe-pool-b
                .subscribeOn(Schedulers.newElastic("subscribe-pool-b"))
                .doOnSubscribe(s -> LogUtil.debug("doOnSubscribe-a"))  // 运行在 subscribe-pool-a
                .subscribeOn(Schedulers.newElastic("subscribe-pool-a"))
                .subscribe(i -> LogUtil.debug(i, "subscribe")); // 运行在 subscribe-pool-b

        DebugUtil.waitMillis(100);
    }

    @Test
    public void subscribeOnTest() {
        Flux.range(1, 3)
                .filter(i -> {
                    LogUtil.debug(i, "filter"); // 运行在 subscribeOn-pool
                    return i % 2 == 0;
                })
                .map(it -> {
                    LogUtil.debug(it, "map"); // 运行在 subscribeOn-pool
                    return it * 2;
                })
                .subscribeOn(Schedulers.newElastic("subscribeOn-pool"))
                .subscribe(it -> {
                    LogUtil.debug(it, "subscribe"); // 运行在 subscribeOn-pool
                });

        DebugUtil.waitMillis(100);
    }

    /**
     * onSubscribe 方法会再当前线程调用
     */
    @Test
    public void subscribeOnDoOnXXTest() {
        Flux.create(new Consumer<FluxSink<Integer>>() {
            {
                // 当前线程
                LogUtil.debug("create");
            }

            @Override
            public void accept(FluxSink<Integer> fluxSink) {
                LogUtil.debug("start send data");
                fluxSink.next(1);
                LogUtil.debug("send one data");
                fluxSink.complete();
            }
        })
                .publishOn(Schedulers.newElastic("publishOn-pool"))

                .doOnSubscribe(subscription -> LogUtil.debug("doOnSubscribe"))
                .doOnRequest(it -> LogUtil.debug("doOnRequest"))
                .doOnEach(it -> LogUtil.debug("doOnEach"))
                .doOnNext(it -> LogUtil.debug(it, "doOnNext"))
                .doOnTerminate(() -> LogUtil.debug("doOnTerminate"))
                .doOnComplete(() -> LogUtil.debug("doOnComplete"))
                .doOnError(e -> LogUtil.debug("doOnError"))
                .doOnCancel(() -> LogUtil.debug("doOnCancel"))

                .subscribeOn(Schedulers.newElastic("subscribeOn-pool"))
                .subscribe(new Subscriber<>() {
                    // onSubscribe 在当前线程调用
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.debug("onSubscribe");
                        // 调用此方法后才开始发送数据
                        s.request(8);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.debug(integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.debug(t, "onError");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.debug("onComplete");
                    }
                });
        DebugUtil.waitMillis(100);
    }

    @Test
    public void onSubscribeTest() {
        Flux.range(0, 10)
                .subscribeOn(Schedulers.newElastic("subscribeOn-pool"))
                .subscribe(new Subscriber<>() {
                    // onSubscribe 在当前线程调用
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.debug("onSubscribe start");
                        // 调用此方法后才开始发射事件
                        s.request(8);
                        LogUtil.debug("onSubscribe end");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.debug(integer);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        DebugUtil.waitMillis(200);
    }

    public void demoTest() {
        Flux.just(1, 2, 3, 4, 5)
                // 过滤出偶数
                .filter(i -> i % 2 == 0)
                .subscribe(i -> {
                    System.out.println(i);
                });
    }

    public static void main(String[] args) {
        Flux.just("hello world")
                .subscribe(i -> System.out.println(i));
    }
}
