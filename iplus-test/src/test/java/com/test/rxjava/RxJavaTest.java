package com.test.rxjava;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import xyz.liujin.iplus.util.LogUtil;

public class RxJavaTest {
    @Test
    public void flowableTest() {
        Flowable.fromPublisher(it -> {
            LogUtil.debug("fromPublisher");
            it.onNext(1);
            it.onComplete();
        })
                .map(it -> {
                    LogUtil.debug("map");
                    return it;
                })
                .doOnSubscribe(it -> {
                    LogUtil.debug("doOnSubscribe");
                })
                .doOnRequest(it -> {
                    LogUtil.debug("doOnRequest");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.debug("onSubscribe");
                        s.request(1000);
                    }

                    @Override
                    public void onNext(Object o) {
                        LogUtil.debug("onNext");
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Test
    public void rxTest() {
        Flowable.just(1)
                .doOnEach(it -> System.out.println("doOnEach"))
                .doFinally(() -> System.out.println("doFinally"))
                .subscribe(it -> System.out.println("subscribe"), it -> System.out.println("errorConsumer"));
    }
}
