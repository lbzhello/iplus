package com.test.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * HIK所有，<br/>
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。<br/>
 *
 * @describtion
 * @copyright Copyright: 2015-2020
 * @creator liubaozhu
 * @create-time 11:57 2022/4/1
 * @department 安检业务部
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user :{修改人} :{修改时间}
 * @modify by reason :{原因}
 **/
public class SchedulersTest {
    /**
     * 线程调度示例
     */
    @Test
    public void schedulersTest() {
        Flux.just(1, 2, 3)
                .map(i -> i + 1)
                // 切换下一个线程池
                .publishOn(Schedulers.newElastic("publish-pool"))
                .map(i -> i * 2)
                // 配置订阅线程池（默认线程池）
                .subscribeOn(Schedulers.newElastic("subscribe-pool"))
                .subscribe(i -> System.out.println(i));

        DebugUtil.sleep(100);
    }

    @Test
    public void ExecutorTest() {
        Executors.newSingleThreadExecutor();
        Executors.newCachedThreadPool();
    }
}
