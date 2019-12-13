package xyz.liujin.iplus.observer;

import io.reactivex.*;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.awt.datatransfer.FlavorListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.activation.Activatable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyObserver {

    public static void main(String[] args) {
        printCurrentThread("main");
        System.out.println(new MyObserver().observableTest());
    }

    public String observableTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Flowable.just("hello2 world")
                .flatMap(text -> {
                    printCurrentThread("flatMap");
                    return Flowable.fromArray(text.split(" "));
                })
                .map(item -> {
                    printCurrentThread("map1");
                    item.charAt(6);
                    return item;
                })
                // 设置默认的调度线程，与位置无关
                .subscribeOn(Schedulers.from(executorService))
                // 设置之后在哪里调度
                .observeOn(Schedulers.newThread())
                .map(item -> {
                    printCurrentThread("map2");
                    return item;
                })
                .doFinally(() ->{
                    printCurrentThread("doFinally");
                })
                .onErrorReturnItem("error....")
                .subscribe(item -> {
                    printCurrentThread("onNext");
                    System.out.println(item);
//                    throw new Exception("333");
                }, throwable ->  {
                    printCurrentThread("onError");
                    System.out.println(throwable);
                }, () -> {
                    printCurrentThread("onComplete");
                });
        return "end";
    }

    public static void printCurrentThread(String method) {
        System.out.println("【" + method + "】 method running in thread: <<" + Thread.currentThread().getName() + ">>");
    }
}
