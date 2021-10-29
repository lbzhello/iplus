package xyz.liujin.iplus.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class B {
    @Autowired
    private A a;

    public String hello() {
        System.out.println("hello");
        return "hello";
    }
}
