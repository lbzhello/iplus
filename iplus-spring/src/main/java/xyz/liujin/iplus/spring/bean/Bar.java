package xyz.liujin.iplus.spring.bean;

import org.springframework.stereotype.Component;

@Component
public class Bar {

    public String hello() {
        System.out.println("hello");
        return "hello";
    }
}
