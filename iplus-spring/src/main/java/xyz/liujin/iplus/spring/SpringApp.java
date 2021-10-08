package xyz.liujin.iplus.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xyz.liujin.iplus.spring.foo.Foo;

@SpringBootApplication
public class SpringApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringApp.class);
        Foo foo = applicationContext.getBean(Foo.class);
        foo.hello();
    }
}
