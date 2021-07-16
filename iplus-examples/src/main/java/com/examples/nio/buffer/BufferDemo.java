package com.examples.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class BufferDemo {

    public static void main(String[] args) {
        ByteBuffer bb = ByteBuffer.allocate(256);

        // 写入数据
        bb.put("hello world".getBytes(StandardCharsets.UTF_8));

        // 转成读模式, limit = position; position = 0
        bb.flip();

        CharBuffer cb = StandardCharsets.UTF_8.decode(bb);
        System.out.println(cb.toString());


        // 转换写模式， limit
        bb.clear();
        bb.put("hihi".getBytes(StandardCharsets.UTF_8));

        bb.flip();
        System.out.println(StandardCharsets.UTF_8.decode(bb).toString());

    }
}
