package com.test.oom;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OOMTest {
    private static final Logger logger = LoggerFactory.getLogger(OOMTest.class);

    private static OutOfMemory outOfMemory = new OutOfMemory();

    // -Xms32m -Xmx32m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=G:/tmp
    @Test
    public void oomTest() {
        logger.debug("app start");
        outOfMemory.foo(33);
        logger.debug("app end");
    }
}
