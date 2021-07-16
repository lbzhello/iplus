package com.examples.nio.buffer;

import java.nio.ByteBuffer;

public class BufferUtils {
    /**
     * buffer 必须 flip 后才能读取，这里可以查看当前数据
     * @param bb
     * @return
     */
    public String string(ByteBuffer bb) {
        return new String(bb.array(), 0, bb.position());
    }
}
