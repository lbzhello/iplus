package xyz.liujin.iplus.webflux

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.SslProvider

import java.util.function.Consumer

WebClient.builder().clientConnector(new ReactorClientHttpConnector(HttpClient.create()
        .secure(new Consumer<SslProvider.SslContextSpec>() {
            @Override
            void accept(SslProvider.SslContextSpec sslContextSpec) {
                sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE))
            }
        }))).build()
        .get().uri("http://www.baidu.com").retrieve().bodyToMono(String.class).doOnNext { System.out.println(it) }.block()