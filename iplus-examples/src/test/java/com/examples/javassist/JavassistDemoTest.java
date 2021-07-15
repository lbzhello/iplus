package com.examples.javassist;

import org.junit.jupiter.api.Test;

public class JavassistDemoTest {


    @Test
    public void createClassTest() {
        JavassistDemo javassistDemo = new JavassistDemo();
        javassistDemo.createClass();
    }

    @Test
    public void updateClassTest() {
        JavassistDemo javassistDemo = new JavassistDemo();
        javassistDemo.updateClass();
    }

    @Test
    public void extendClassTest() {
        JavassistDemo javassistDemo = new JavassistDemo();
        javassistDemo.extendClass();
    }
}
