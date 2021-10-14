package xyz.liujin.iplus.test.stream;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.stream.Stream;

public class NullTest {
    /**
     * Stream 中的元素可以为 null
     */
    @Test
    public void nullTest() {
        Stream.of("hello", null, "world")
                .filter(Objects::nonNull)
                .forEach(it -> System.out.println(it));
    }
}
