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
public class FluxTest {

    /**
     * from 不支持背压
     * from 不支持 publishOn 切换线程池
     */
    @Test
    public void fromTest() {
        Flux.from(new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> s) {
                s.onNext(0);
                s.onNext(1);
                s.onNext(2);
                s.onNext(3);
                s.onComplete();
            }
        })
                .map(it -> {
                    LogUtil.debug(it, "map");
                    return it * 2;
                })
//                .publishOn(Schedulers.newElastic("filter-pool"))
                .filter(it -> {
                    LogUtil.debug(it, "filter");
                    return it % 2 == 0;
                })
                .subscribeOn(Schedulers.newElastic("from-pool"))
                .subscribe(it -> LogUtil.debug(it, "subscribe"));

        DebugUtil.waitMillis(200);
    }

    @Test
    public void subscribeOnTest() {
        Flux.range(1, 2)
                .filter(it -> {
                    LogUtil.debug(it, "filter");
                    return it % 2 == 0;
                })
                .subscribeOn(Schedulers.newElastic("subscribe-pool-c"))
                .doOnSubscribe(s -> LogUtil.debug("doOnSubscribe-b"))
                .subscribeOn(Schedulers.newElastic("subscribe-pool-b"))
                .doOnSubscribe(s -> LogUtil.debug("doOnSubscribe-a"))
                .subscribeOn(Schedulers.newElastic("subscribe-pool-a"))
                .subscribe(i -> LogUtil.debug(i));
        DebugUtil.waitMillis(100);
    }

    @Test
    public void createPublishTest() {
        Flux.just(1, 2, 3)
                .subscribe(System.out::println);
    }

    /**
     * onSubscribe 方法会再当前线程调用
     */
    @Test
    public void createDoOnXXTest() {
        Flux.create(new Consumer<FluxSink<Integer>>() {
            {
                LogUtil.debug("create");
            }

            @Override
            public void accept(FluxSink<Integer> fluxSink) {
                LogUtil.debug("start send data");
                fluxSink.next(1);
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
