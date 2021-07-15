package com.examples.nio.buffer;

import java.nio.CharBuffer;

/**
 *
 */
public class BufferEg {

    public static void main(String[] args) {
        CharBuffer charBuffer = CharBuffer.allocate(128);
        charBuffer.append("你好");

        System.out.println();
    }
}
