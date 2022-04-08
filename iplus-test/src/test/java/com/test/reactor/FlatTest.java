package com.test.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

/**
 * HIK所有，<br/>
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。<br/>
 *
 * @describtion
 * @copyright Copyright: 2015-2020
 * @creator 姓名
 * @create-time 11:50 2020/10/26
 * @department 安检业务部
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user :{修改人} :{修改时间}
 * @modify by reason :{原因}
 **/
public class FlatTest {
    private static final Logger logger = LoggerFactory.getLogger(FlatTest.class);

    /**
     * consumer 出现异常
     */
    @Test
    public void errorTest() {
        Flux.range(1, 5)

                // 出现异常时调用，只能铺货前面的异常
                .doOnError(t -> {
                    LogUtil.debug(t, "doOnError");
                })
                .map(it -> {
                    LogUtil.debug(it, "map");
                    throw new RuntimeException("map error");
                })
                //
                .doFinally(it -> {
                    LogUtil.debug(it, "doFinally1");
                })
                .doFinally(it -> {
                    LogUtil.debug(it, "doFinally2");
                })
                .subscribe(it -> {
                    LogUtil.debug(it, "consumer");
                    throw new RuntimeException("consumer error");
                }, e -> {
                    LogUtil.debug(e, "onError");
                }, () -> {
                    LogUtil.debug("complete");
                });

        DebugUtil.sleep(100);
    }

    /**
     * Flux 中的元素不能是 null
     */
    @Test
    public void nullTest() {
        Flux.just("hello", null, "world")
                .filter(Objects::nonNull)
                .subscribe(it -> {
                    System.out.println(it);
                });
    }

    /**
     * stream 不可重用
     */
    @Test
    public void streamTest() {
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4);
        integerStream.forEach(it -> {
            System.out.println(it);
        });
        System.out.println("again");
        integerStream.forEach(it -> {
            System.out.println(it);
        });
    }

    @Test
    public void concatTest() {
        Flux.concat(flux("a"), flux("b"))
                .subscribe(it -> {
                    System.out.println(it);
                });
    }

    @Test
    public void zipTest() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        Flux.merge(flux("a"), flux("b"))
                .subscribe(it -> {
                    queue.offer(it);
                });
        // 等待子线程处理完成
        Thread.sleep(1000);
        System.out.println();
    }

    /**
     * flatMap
     */
    @Test
    public void flatTest() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        Flux.just("a", "b", "c", "d")
                .flatMap(this::flux)
                .take(1)
                .subscribe(it -> {
                    queue.offer(it);
                });
        // 等待子线程处理完成
        Thread.sleep(1000);
        System.out.println();

    }

    /**
     * 随意延迟一段时间
     * @param v
     * @return
     */
    private Flux<String> flux(String v)  {
        return Flux.just(v)
                // 随机停顿一段时间，模拟业务处理
                .doOnNext(it -> {
                    try {
                        long l = Double.valueOf(Math.random() * 1000).longValue();
                        Thread.sleep(l);
                        System.out.println(v + ":" + l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .subscribeOn(Schedulers.newElastic("testThread"));
    }

    @Test
    public void randTest() {
        int i = Double.valueOf(Math.random() * 100).intValue();
        System.out.println(i);
    }
}
