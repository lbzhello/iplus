package xyz.liujin.iplus.test.reactor;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import xyz.liujin.iplus.test.TestHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
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
public class ReactorTest {

    @Test
    public void delayTest() throws InterruptedException {
        Flux.range(1, 100)
                .delayElements(Duration.ofSeconds(1))
                .subscribe(it -> System.out.println(it));
        Thread.sleep(20*1000);
        System.out.println();
    }

    @Test
    public void requestTest() {
        Flux.range(1, 100)
                .subscribe(new Subscriber<Integer>() {
                    private Subscription subscription;
                    private int count = 0;
                    private int BATCH_SIZE = 10; // 消费者处理速度

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(BATCH_SIZE); // 必须调用此方法订阅者才开始处理数据
                    }

                    @Override
                    public void onNext(Integer integer) {
                        count++;
                        System.out.println(integer);
                        if (count >= BATCH_SIZE) {
                            count = 0; // 从新计数
                            System.out.println("request more " + BATCH_SIZE);
                            subscription.request(BATCH_SIZE); // 处理完成，继续请求更多的数据
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    @Test
    public void pubSubTest() {
        Flux.create(it -> {
            try {
                it.next("hello1");
                Thread.sleep(1000);
                it.next("hello2");
                Thread.sleep(2000);
                it.next("hello3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            it.complete();
        })
                .publishOn(Schedulers.newElastic("publishThread"))
                .subscribeOn(Schedulers.newElastic("subscribeThread"))
                .subscribe(it -> {
                    TestHelper.printCurrentThread("subscribe");
                    System.out.println("received");
                    System.out.println(it);
                });
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


    /**
     * Subscription#request 方法调用后才能发射事件，发射数小于请求数；
     * Subscriber#onScribe 方法不受线程调度影响；
     */
    @Test
    public void backPressureTest() {
//        Flux.interval(Duration.ofSeconds(1))
        Flux.create((Consumer<FluxSink<Integer>>) it -> {
            TestHelper.printCurrentThread("create");
            Stream.of(1, 2, 3, 4, 5, 6, 7).forEach(elem -> it.next(elem));
            it.complete();
        })
                .subscribeOn(Schedulers.newElastic("subscribeOnThread"))
                .publishOn(Schedulers.newElastic("publishOnThread"))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        TestHelper.printCurrentThread("onSubscribe");
                        System.out.println("onSubscribe");
                        s.request(5);
                    }

                    @Override
                    public void onNext(Integer aLong) {
                        System.out.println(aLong);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
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
