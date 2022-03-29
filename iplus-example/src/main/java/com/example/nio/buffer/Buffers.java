package com.example.nio.buffer;

import java.nio.ByteBuffer;

/*自定义Buffer类中包含读缓冲区和写缓冲区，用于注册通道时的附加对象*/
public class Buffers {

    ByteBuffer readBuffer;
    ByteBuffer writeBuffer;

    Buffers(int readCapacity, int writeCapacity){
        readBuffer = ByteBuffer.allocate(readCapacity);
        writeBuffer = ByteBuffer.allocate(writeCapacity);
    }

    public static final Buffers allocate(int readCapacity, int writeCapacity) {
        return new Buffers(readCapacity, writeCapacity);
    }

    public ByteBuffer getReadBuffer(){
        return readBuffer;
    }

    public ByteBuffer gerWriteBuffer(){
        return writeBuffer;
    }
}
