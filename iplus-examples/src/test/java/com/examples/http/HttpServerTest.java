package com.examples.http;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.examples.JavaApp;

public class HttpServerTest {
    @Test
    public void httpServerTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaApp.class);
        FooHttpServer httpServer = applicationContext.getBean(FooHttpServer.class);
        httpServer.start(8080);
        System.out.println();
    }
}
