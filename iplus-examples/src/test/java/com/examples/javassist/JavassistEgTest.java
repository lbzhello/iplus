package com.examples.javassist;

import org.junit.jupiter.api.Test;

public class JavassistEgTest {


    @Test
    public void createClassTest() {
        JavassistEg javassistEg = new JavassistEg();
        javassistEg.createClass();
    }

    @Test
    public void updateClassTest() {
        JavassistEg javassistEg = new JavassistEg();
        javassistEg.updateClass();
    }

    @Test
    public void extendClassTest() {
        JavassistEg javassistEg = new JavassistEg();
        javassistEg.extendClass();
    }
}
