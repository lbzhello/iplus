package xyz.liujin.iplus.test.rxjava;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class ErrorTest {
    private static final Logger logger = LoggerFactory.getLogger(ErrorTest.class);

    /**
     * consumer 中的异常可以被 errorConsumer 捕获，并结束流的发射
     */
    @Test
    public void errorConsumerTest() {
        Flux.just(1, 2, 3)
                .subscribe(it -> {
                    System.out.println(it);
                    throw new IllegalArgumentException("consumer error");
                }, e -> {
                    logger.error("errorConsumer", e);
                });
    }
}
