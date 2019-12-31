package xyz.liujin.iplus;

import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;
import xyz.liujin.iplus.lombok.Foo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * HIK所有，<br/>
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。<br/>
 *
 * @describtion
 * @copyright Copyright: 2015-2020
 * @creator 姓名
 * @create-time 10:29 2019/12/14
 * @department 安检业务部
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user :{修改人} :{修改时间}
 * @modify by reason :{原因}
 **/
public class LocalTest {

    @Test
    public void localTest() {

    }

    @Test
    public void groovyTest() {

    }

    @Test
    public void lombokTest() {
        Foo foo1 = new Foo(); // 生成了无参构造器
        foo1.setAge(2); // 生成了 setter

        Foo foo2 = new Foo();
        foo2.setAge(2);

        // 结果为 true, 因为根据所有的字段（非 transient）重写了 equals 方法
        System.out.println(foo1.equals(foo2));

        // Foo(name=null, age=2) 重写了 toString 方法
        System.out.println(foo1.toString());
    }

    @Test
    public void webClientTest() {
        WebClient.create().get().uri("http://www.baidu.com").exchange().flatMap(it -> it.bodyToMono(String.class)).doOnNext(it -> System.out.println(it)).block();
        WebClient.create().get().uri("http://www.baidu.com").retrieve().bodyToMono(String.class).doOnNext(it -> System.out.println(it)).block();
    }

    @Test
    public void processorTest() {
        EmitterProcessor<Object> emitterProcessor = EmitterProcessor.create();
        emitterProcessor.publishOn(Schedulers.newElastic("newThread")).map(it -> {
            TestHelper.printCurrentThread("map");
            return it;
        }).onErrorContinue((e, it) -> {
            System.out.println(it);
        }).subscribe(it -> {
            TestHelper.printCurrentThread("subscribe");
            System.out.println(it);
        }, e -> {
            TestHelper.printCurrentThread("onError");
            System.out.println(e.getMessage());
        });

        emitterProcessor.onNext(1);
        emitterProcessor.onNext(1);
        emitterProcessor.onNext(1);
        emitterProcessor.onComplete();


    }

    @Test
    public void flowableTest() {
        Flowable.just(1, 2, 4)

                .subscribe(it -> System.out.println(it));

        Flux.just(1, 2, 4)
                .subscribe(it -> System.out.println(it));
    }

    @Test
    public void fromTest() {
        Flux.from((Publisher<String>) it -> {
            it.onNext("22");
            it.onNext("23");
//            it.onError(new Throwable("error"));
            it.onNext("24");
            it.onComplete();
            // from 不支持异步 用 create
        }).publishOn(Schedulers.newElastic("my")).map(it -> {
            System.out.println(it);
            return it;
        }).subscribe(it -> {
            System.out.println("subscribe:" + it);
        }, e -> {
            System.out.println(e.getMessage());
        }, () -> {
            System.out.println("end");
        });
    }

    @Test
    public void collectTest() {
        Flux.fromIterable(Arrays.asList(1, 2, 3, 4))
                .map(it -> it)
                .collect(() -> new ArrayList<Integer>(), (o, i) -> {
                    System.out.println(o.add(i));
                })
                .subscribe(it -> {
                    System.out.println(it);
                });
    }

    @Test
    public void groupTest() {
        Flux.fromIterable(Arrays.asList("aaa", "cc", "bb", "dddd", "e"))
                .distinct()
                .groupBy(elem -> {
                    return elem.length();
                })
                .flatMap(elem -> {
                    return elem.collectList();
                })
                .map(it -> {
                    return it;
                })
                .onErrorContinue((e, o) -> {})
                .subscribe(items -> {
                    System.out.println(items.get(0));
                });
    }

    @Test
    public void publishTest() {
        Flux.create((Consumer<FluxSink<Integer>>) it -> {
            TestHelper.printCurrentThread("create");
            Arrays.asList(1, 2, 3).forEach(elem -> it.next(elem));
            // 需要加上这句，否则无法处理完成
            it.complete();
        }).publishOn(Schedulers.newElastic("newThread")).map(it -> {
            TestHelper.printCurrentThread("map");
            return it;
        }).onErrorContinue((e, it) -> {
            System.out.println(it);
        }).subscribe(it -> {
            TestHelper.printCurrentThread("subscribe");
            System.out.println(it);
        }, e -> {
            TestHelper.printCurrentThread("onError");
            System.out.println(e.getMessage());
        });
    }

    @Test
    public void fluxTest() {
        Flux.from(it -> {
            it.onNext("hello");
        }).subscribe(it -> {
            System.out.println(it);
        });
    }


}
