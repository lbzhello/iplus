package xyz.liujin.iplus.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xyz.liujin.iplus.spring.foo.Foo;

@ComponentScan
@Configuration
public class SpringApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringApp.class);
        Foo foo = applicationContext.getBean(Foo.class);
        foo.sayHello();
    }
}
