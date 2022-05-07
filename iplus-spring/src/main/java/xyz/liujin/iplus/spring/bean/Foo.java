package xyz.liujin.iplus.spring.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(FooImportA.class)
@Configuration
public class Foo {
    @Bean
    public FooBeanA fooBeanA() {
        return new FooBeanA();
    }
}
