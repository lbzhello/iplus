package com.examples.javassist;

/**
 * javassist 修改类测试
 */
public class HelloImpl implements Hello {
    @Override
    public String hello() {
        System.out.println("method running...");
        return "hello";
    }
}
