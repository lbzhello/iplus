package xyz.liujin.iplus;

import com.google.common.collect.Lists;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.reactivex.Flowable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import xyz.liujin.iplus.lombok.Foo;
import xyz.liujin.iplus.util.FileUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        ArrayList<String> list = Lists.newArrayList("dcb", "c22f", "c33", "f55");
        Collections.sort(list);
        System.out.println(list);

    }

    @Test
    public void streamTest() {
        List<Integer> collect = Stream.iterate(1, it -> it * 2).limit(5).collect(Collectors.toList());
        System.out.println();
    }

    @Test
    public void windowTest() {
        Flux.just('1', '2', '*', '@', '5', '6', '(', '(', '(', '7')
                .filter(it -> it == '@')
                .window(it -> {
                    it.onNext(7);
                    it.onNext('7');
                })
                .flatMap(it -> it)
                .subscribe(it -> {
                    if (it == '(') {
                        Flux.create(sink ->{

                        });
                    }
                    System.out.println(it);
                });
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
    public void sslWebClientTest() {
        WebClient.builder().clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                .secure(it -> it.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE))))).build()
                .get().uri("http://www.baidu.com").retrieve().bodyToMono(String.class).doOnNext(it -> System.out.println(it)).block();
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
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.schedulers.Schedulers.computation())
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
    public void backPresureTest() {
        EmitterProcessor<Object> emitterProcessor = EmitterProcessor.create();

    }

    @Test
    public void completableFutureTest() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            return "hello world";
        }, Executors.newSingleThreadExecutor()).thenApply(it -> {
            TestHelper.printCurrentThread("thenApply");
            return it + 1;
        });
    }


    /**
     * 测试异步操作，将多个异步 Observable 结果合并成一个
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void zipTest() throws IOException, InterruptedException {

        try (FileWriter fileWriter = new FileWriter("G:/tmp.txt")) {

            Flux<String> flux1 = Flux.just("a", "b", "c", "d")
                    .subscribeOn(Schedulers.newElastic("flux1"))
                    .map(it -> {
                        try {
                            TestHelper.printCurrentThread("flux1");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return it;
                    });

            Flux<Long> flux2 = Flux.interval(Duration.ofSeconds(1))
                    .subscribeOn(Schedulers.newElastic("flux2"))
                    .map(it -> {
                        TestHelper.printCurrentThread("flux2");
                        return it;
                    });

            Flux.zip(flux1, flux2)
                    .publishOn(Schedulers.newElastic("newThread"))
                    .subscribe(it -> {
                        TestHelper.printCurrentThread("zip");
                        System.out.println(it);
                    });

            for (; ; ) {
                Thread.sleep(1000);
                TestHelper.printCurrentThread("current");
            }
        }

    }

    @Test
    public void threadTest() {
        Flux
                .create(it -> {
                    TestHelper.printCurrentThread("from");
                    it.next(1);
                    it.complete();
                })
                .doOnSubscribe(it -> TestHelper.printCurrentThread("doOnSubscribe1"))
                .subscribeOn(Schedulers.newElastic("subscribeOn1"))
                .doOnSubscribe(it -> TestHelper.printCurrentThread("doOnSubscribe2"))
                .subscribeOn(Schedulers.newElastic("subscribeOn2"))
                .doOnNext(it -> TestHelper.printCurrentThread("doOnNext1"))
                .publishOn(Schedulers.newElastic("publishOn1"))
                .doOnSubscribe(it -> TestHelper.printCurrentThread("doOnSubscribe3"))
                .doOnNext(it -> TestHelper.printCurrentThread("doOnNext2"))
                .subscribe(it -> TestHelper.printCurrentThread("subscribe"));
    }

    @Test
    public void doOnEachTest() {
        Mono.just(1)
                .map(it -> {
                    System.out.println("map");
                    return it;
                })
                .doOnEach(it -> {
                    System.out.println("it: " + it.get());
                    System.out.println("doOnEach");
                })
                .doOnNext(it -> System.out.println("doOnNext"))
                .doFinally(it -> System.out.println("doFinally"))
                .subscribe(it -> System.out.println("subscribe"), it -> System.out.println("errorConsumer"));
    }

    @Test
    public void rxTest() {
        Flowable.just(1)
                .doOnEach(it -> System.out.println("doOnEach"))
                .doFinally(() -> System.out.println("doFinally"))
                .subscribe(it -> System.out.println("subscribe"), it -> System.out.println("errorConsumer"));
    }

    @Test
    public void finallyTest() {
        Map<String, String> map = new HashMap<>();
        Flux
                .fromIterable(Collections.emptyList())
//                .create(it -> {
//            it.next(1);
//            it.complete();
//            throw new IllegalStateException("");
//        })
//                .doOnEach(it -> {
////                    throw new IllegalStateException("hhhh");
////                    return it;
//                })

//                .doOnError(it -> {
//                    System.out.println("doOnError");
//                })
//                .onErrorContinue((it, o) -> {
//                    System.out.println("onErrorContinue");
//                })
                .doFinally(it -> {
                    System.out.println("doFinally");
                })
                .doOnSubscribe(it -> System.out.println("doOnSubscribe"))
                .subscribe(it -> {
//                    throw new IllegalStateException("sssss");
                    System.out.println("consumer");
                }, it -> System.out.println(it.getMessage()), () -> System.out.println("completeConsumer"));
    }

    @Test
    public void justTest() {
//        Flux.from(it -> {
//            TestHelper.printCurrentThread("from");
//            it.onNext("2");
//            it.onNext("3");
//            it.onNext("4");
//            it.onNext("5");
//            it.onComplete();
////            throw new IllegalStateException("from-error");
////            it.complete();
//        })
        Flux.just("1", "2", "3", "4")
//                .subscribeOn(Schedulers.newElastic("subscribeThread"))
                .doOnNext(it -> {
                    if (it.equals("2")) {
                        throw new IllegalStateException("map-error");
                    }
                })
                .doOnError((t) -> System.out.println("doOnError: " + t.getMessage()))
                .onErrorContinue((t, o) -> System.out.println("onErrorContinue: " + t.getMessage()))
                .map(it -> {
                    if (it.equals("3")) {
                        throw new IllegalStateException("map-error");
                    }
                    return it;
                })
                .subscribe(it -> {
                    System.out.println(it);
//                    throw new IllegalStateException("subscribe-error");
                }, it -> System.out.println("errorConsumer"));

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
        })
                .subscribeOn(Schedulers.newElastic("subscribeOn"))
                .publishOn(Schedulers.newElastic("publishOnThread")).map(it -> {
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
    public void publishSubscribeTest() {
        Flux.create(it -> {
            TestHelper.printCurrentThread("from");
            Arrays.asList(1, 2, 3).forEach(elem -> {
                System.out.println(elem);
                it.next(elem);
            });
            // 需要加上这句，否则无法处理完成
            it.complete();
        }).publishOn(Schedulers.newElastic("publishThread")).map(it -> {
            TestHelper.printCurrentThread("map1");
            return it;
        // subscribeOn 设定默认线程，与所处位置无关
        // 因此下面的 Map2 依然运行在 publishOn 线程
        }).subscribeOn(Schedulers.newElastic("subscribeThread")).map(it -> {
            TestHelper.printCurrentThread("map2");
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


}
