package com.test.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;
import xyz.liujin.iplus.util.math.MathUtil;

/**
 * Reactor 使用示例
 */
public class UsageExampleTest {

    /**
     * A -> B, C
     * D -> B
     * C, E -> F
     *
     * A, D -> B, C, E -> F
     */
    @Test
    public void abcdef() {
        Flux.zip(task("A"), task("D"))
                .flatMap(ad -> Flux.zip(task("B"), task("C"), task("E")))
                .flatMap(ecd -> task("F"))
                .subscribe();

//        // D -> B
//        Mono<String> db = task("D").flatMap(d -> task("B"));
//
//        // C, E -> F
//        Flux<String> cef = Flux.zip(task("C"), task("E"))
//                .flatMap(ce -> task("F"));
//
//        // A -> B, C
//        task("A")
//                .flux()
//                .flatMap(a -> Flux.zip(db, cef))
//                .subscribe();
        DebugUtil.sleep(1000);
    }

    /**
     * A -> B, C
     */
    @Test
    public void a_to_b_c() {
        task("A")
                .flux()
                .flatMap(it -> Flux.zip(task("B"), task("C")))
                .subscribe();

        DebugUtil.sleep(1000);
    }


    /**
     * B, C -> A
     */
    @Test
    public void b_c_to_a() {
        Flux.zip(task("B"), task("C"))
                .flatMap(it -> task("A"))
                .subscribe();

        DebugUtil.sleep(1000);
    }
    
    public Mono<String> task(String task) {
        return Mono.just(task)
                .doOnNext(it -> {
                    LogUtil.debug("task start " + it);

                    // 模拟任务耗时
                    int random = MathUtil.randomInt(100);
                    DebugUtil.sleep(random);

                    LogUtil.debug("task end " + it);
                })
                .subscribeOn(Schedulers.newElastic("task-pool-" + task));

    }
}
