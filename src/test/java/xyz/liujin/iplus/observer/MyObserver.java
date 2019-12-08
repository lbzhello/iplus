package xyz.liujin.iplus.observer;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyObserver {
    @Test
    public void mainTest() {
        printCurrentThread("main");
        System.out.println(observableTest());
    }

    @Test
    public String observableTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Observable.just("hello world")
                .map(text -> {
                    printCurrentThread("map");
                    Path tempFile = Files.createTempFile(null, ".txt");
                    Files.writeString(tempFile, text);
                    try {

                        String none = null;
                        none.length();
                    } catch (Throwable throwable) {
                        System.out.println("========== error =============");
                    }
                    return tempFile;
                })
//                .subscribeOn(Schedulers.from(executorService))
//                .observeOn(Schedulers.from(executorService))
                .subscribe(file -> {
                    printCurrentThread("subscribe");
                    System.out.println(Files.readAllLines(file));
                }, throwable ->  {
                    System.out.println(throwable.getMessage());
                });
        return "end";
    }

    public static void printCurrentThread(String method) {
        System.out.println("【" + method + "】 method running in thread: <<" + Thread.currentThread().getName() + ">>");
    }
}
