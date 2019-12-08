package xyz.liujin.iplus.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, Object> cache() {
        return CacheBuilder.newBuilder()
                // 初始大小，合理的设置可以避免 resize 操作，但会占用占用初始空间
                .initialCapacity(128)
                // 并发数
                .concurrencyLevel(5)
                // 写入后 10 分钟过期
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }
}
