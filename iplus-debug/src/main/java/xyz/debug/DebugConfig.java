package xyz.debug;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import xyz.debug.aop.ControllerAspect;
import xyz.debug.aop.CustomAspect;

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
