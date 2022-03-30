package xyz.liujin.iplus.test.reactor;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.scheduling.annotation.Schedules;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;
import xyz.liujin.iplus.util.thread.ThreadPool;

import java.util.concurrent.ExecutorService;
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

    @Test
    public void subscribeOnTest() {
        Flux.just("hello world")
                .subscribeOn(Schedulers.newElastic("hello-test"))
                .subscribe(i -> LogUtil.debug(i));
        DebugUtil.waitMillis(100);
    }

    /**
     * onSubscribe 方法会再当前线程调用
     */
    @Test
    public void createDoOnSubscribeTest() {
        Flux.create((Consumer<FluxSink<Integer>>)  fluxSink -> {
            LogUtil.debug("create");
            fluxSink.next(1);
            fluxSink.next(2);
            fluxSink.complete();
        })
                .doOnSubscribe(subscription -> {
                    LogUtil.debug("doOnSubscribe");
                })
                .subscribeOn(Schedulers.newElastic("subs-pool"))
                .subscribe(new Subscriber<>() {
                    // onSubscribe 再当前线程调用
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.debug("onSubscribe start");
                        s.request(10);
                        LogUtil.debug("onSubscribe end");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.debug("it", "onNext");
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

    @Test
    public void onSubscribeTest() {
        Flux.range(0, 10)
                .subscribeOn(Schedulers.newElastic("subscribeOnThread"))
                .subscribe(new Subscriber<>() {
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

    public static void main(String[] args) {
        Flux.just("hello world")
                .subscribe(i -> System.out.println(i));
    }
}
