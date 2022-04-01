package com.test.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.util.LogUtil;
import xyz.liujin.iplus.util.debug.DebugUtil;

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
    @Test
    public void schedulersTest() {
        Flux.just(1, 2)
                .subscribeOn(Schedulers.fromExecutor(Executors.newCachedThreadPool()))
                .subscribeOn(Schedulers.newParallel("p", 2))
                .subscribe(it -> LogUtil.debug(it));

        DebugUtil.sleep(100);
    }
}
