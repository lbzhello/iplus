package xyz.debug;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import xyz.debug.aop.ControllerAspect;
import xyz.debug.aop.CustomAspect;

/**
 * 以 SpringFactoriesLoader 方式使用时，用 @Bean 声明需要加载的 bean;
 */
@Configuration
@EnableAspectJAutoProxy
public class DebugConfig {
    @Bean
    public ControllerAspect controllerAspect() {
        return new ControllerAspect();
    }

    @Bean
    public CustomAspect customAspect() {
        return new CustomAspect();
    }
}
