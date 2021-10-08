package xyz.liujin.iplus.spring.foo;

import org.springframework.stereotype.Component;

@Component
public class Foo {
    public String hello() {
        System.out.println("hello");
        return "hello";
    }
}
