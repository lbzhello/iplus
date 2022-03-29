package com.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ConcurrentUtils {
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentUtils.class);

    /**
     * cyclicBarrier.await 操作
     * @see CyclicBarrier#await()
     * @param cyclicBarrier
     */
    public static void barrierAwait(@Nullable CyclicBarrier cyclicBarrier) {
        if (Objects.isNull(cyclicBarrier)) {
            return;
        }

        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            logger.error("await", e);
        }
    }
}
