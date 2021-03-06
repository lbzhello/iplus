package xyz.liujin.iplus.java;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xyz.liujin.iplus.java.http.FooHttpServer;

@ComponentScan
@Configuration
public class JavaApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaApp.class);
        FooHttpServer httpServer = applicationContext.getBean(FooHttpServer.class);
        httpServer.start(8080);
        System.out.println();
    }
}
