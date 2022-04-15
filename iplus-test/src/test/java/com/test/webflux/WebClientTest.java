package com.test.webflux;

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
import xyz.liujin.iplus.util.LogUtil;

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
public class WebClientTest {
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


}
