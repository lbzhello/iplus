package com.example.aio;

import org.junit.jupiter.api.Test;

public class AsynchronousServerSocketChannelTest {
    @Test
    public void startAsyncServerSocket() {
        AsynchronousServerSocketChannelDemo asynchronousServerSocketChannelDemo = new AsynchronousServerSocketChannelDemo();
        asynchronousServerSocketChannelDemo.startAsynchronousServerSocket();
    }

    @Test
    public void startAsyncSocket() {
        AsynchronousSocketChannelDemo asynchronousSocketChannelDemo = new AsynchronousSocketChannelDemo();
        asynchronousSocketChannelDemo.startAsynchronousSocket();
    }
}
