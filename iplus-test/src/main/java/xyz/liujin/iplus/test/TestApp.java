package xyz.liujin.iplus.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liujin.iplus.test.jvm.Foo;

public class TestApp {
    private static final Logger logger = LoggerFactory.getLogger(TestApp.class);

    private static Foo foo = new Foo();

    public static void main(String[] args) {
        logger.debug("app start");
        foo.foo(33);
        logger.debug("app end");
    }
}
