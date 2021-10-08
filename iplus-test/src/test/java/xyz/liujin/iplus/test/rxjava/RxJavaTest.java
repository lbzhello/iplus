package xyz.liujin.iplus.test.rxjava;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import xyz.liujin.iplus.test.TestHelper;

public class RxJavaTest {
    @Test
    public void flowableTest() {
        Flowable.fromPublisher(it -> {
            TestHelper.printCurrentThread("fromPublisher");
            it.onNext(1);
            it.onComplete();
        })
                .map(it -> {
                    TestHelper.printCurrentThread("map");
                    return it;
                })
                .doOnSubscribe(it -> {
                    TestHelper.printCurrentThread("doOnSubscribe");
                })
                .doOnRequest(it -> {
                    TestHelper.printCurrentThread("doOnRequest");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        TestHelper.printCurrentThread("onSubscribe");
                        s.request(1000);
                    }

                    @Override
                    public void onNext(Object o) {
                        TestHelper.printCurrentThread("onNext");
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
