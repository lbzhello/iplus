package com.examples;

import com.examples.http.HttpServerDemo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
public class JavaApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaApp.class);
        HttpServerDemo httpServer = applicationContext.getBean(HttpServerDemo.class);
        httpServer.start(8080);
        System.out.println();
    }
}
