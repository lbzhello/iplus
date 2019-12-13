package xyz.liujin.iplus.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.TestHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HIK所有，<br/>
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。<br/>
 *
 * @describtion
 * @copyright Copyright: 2015-2020
 * @creator 姓名
 * @create-time 16:47 2019/12/13
 * @department 安检业务部
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user :{修改人} :{修改时间}
 * @modify by reason :{原因}
 **/
public class ReactorTest {
    @Test
    public void fluxTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Flux.fromArray(getArray())
                .map(ele -> {
                    TestHelper.printCurrentThread("map1");
                    ele.charAt(1);
                    return ele;
                })
                .doFinally(signalType -> {
                    TestHelper.printCurrentThread("doFinally");
                    System.out.println(signalType);
                })
                .map(ele -> {
                    TestHelper.printCurrentThread("map2");
                    return ele;
                })
                .onErrorContinue((t, o) -> {
                    TestHelper.printCurrentThread("onErrorContinue");
                    System.out.println("(" + t + ", " + o + ")");
                })
                .subscribeOn(Schedulers.fromExecutor(executorService))
                .subscribe(item -> {
                    TestHelper.printCurrentThread("subscribe");
                    System.out.println(item);
                }, throwable -> {
                    TestHelper.printCurrentThread("onError");
                    System.out.println(throwable.getMessage());
                });
    }

    public String[] getArray() {
        TestHelper.printCurrentThread("getArray");
        return new String[]{"aaa", "bbb", "ccc"};
    }
}
