package xyz.liujin.iplus.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xyz.liujin.iplus.spring.bean.B;

@SpringBootApplication
public class SpringApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringApp.class);
        B b = applicationContext.getBean(B.class);
        b.hello();
    }
}
