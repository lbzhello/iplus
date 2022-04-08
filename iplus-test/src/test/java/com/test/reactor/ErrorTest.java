package com.test.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import xyz.liujin.iplus.util.LogUtil;

public class ErrorTest {
    @Test
    public void onErrorStopTest() {
        Flux.range(1, 5)
                .doOnNext(it -> {
                    LogUtil.debug(it, "doOnNext");
                    if (true) {
                        throw new RuntimeException("doOnNext error");
                    }
                })
                .onErrorStop()
                .subscribe(it -> {
                    LogUtil.debug(it, "subscribe");
                }, t -> {
                    LogUtil.debug(t, "onError");
                });
    }

    /**
     * 出现异常时，转到下一个数据流（相当于转到异常处理的流程）
     * 不会再进行当前的数据流
     */
    @Test
    public void onErrorResumeTest() {
        Flux.range(1, 5)
                .map(it -> {
                    LogUtil.debug(it, "map");
                    if (true) {
                        throw new RuntimeException("map error");
                    }
                    return it;
                })
                // 出现异常时，转到下一个数据流
                // 不会再进行当前的数据流
                .onErrorResume(t -> Flux.just(10, 11, 12))
                .subscribe(it -> {
                    LogUtil.debug(it, "consumer");
                });
    }

    /**
     * 出现异常时跳过，继续发射下一个元素，类似 java continue
     * 只能捕获前面的异常
     * 之后算正常流成，不会再调用 onError
     */
    @Test
    public void onErrorContinueTest() {
        Flux.range(1, 5)
                .map(it -> {
                    LogUtil.debug(it, "map");
                    throw new RuntimeException("map error");
                })
                // 出现异常时跳过，继续发射下一个元素，类似 continue
                // 只能捕获前面的异常
                .onErrorContinue((t, obj) -> {
                    LogUtil.debug(t, "onErrorContinue");
                })
                .subscribe(it -> {
                    LogUtil.debug(it, "consumer");
                }, t -> {
                    LogUtil.debug(t, "onError");
                }, () -> {
                    LogUtil.debug("onComplete");
                });
    }

    /**
     * 出现异常时调用;
     * 只能捕获前面的异常;
     */
    @Test
    public void doOnErrorTest() {
        Flux.range(1, 5)
                .map(it -> {
                    LogUtil.debug(it, "map");
                    throw new RuntimeException("map error");
                })
                // 出现异常时调用，只能捕获前面的异常
                .doOnError(t -> LogUtil.debug(t, "doOnError"))
                .subscribe(it -> {
                    LogUtil.debug(it, "consumer");
                }, t -> {
                    LogUtil.debug(t, "onError");
                }, () -> {
                    LogUtil.debug("onComplete");
                });
    }
}
