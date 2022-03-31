package com.test.clazz;

import org.junit.jupiter.api.Test;

public class ClassTest {
    @Test
    public void classEqualTest() {
        Object foo1 = new Foo();
        Foo foo2 = new Foo();
        Object bar = new Bar();

        System.out.println(foo1.getClass() == foo2.getClass());
        System.out.println(foo1.getClass() == bar.getClass());
    }
}
