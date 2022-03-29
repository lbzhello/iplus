package com.example.http;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.example.JavaApp;

public class HttpServerTest {
    @Test
    public void httpServerTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaApp.class);
        HttpServerDemo httpServer = applicationContext.getBean(HttpServerDemo.class);
        httpServer.start(8080);
        System.out.println();
    }
}
