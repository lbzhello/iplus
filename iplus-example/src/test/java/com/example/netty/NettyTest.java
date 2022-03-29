package com.example.netty;

import org.junit.jupiter.api.Test;

public class NettyTest {
    @Test
    public void startNettyServer() {
        NettyServerDemo nettyServerDemo = new NettyServerDemo();
        nettyServerDemo.startServer();
    }

    @Test
    public void startNettyClient() {
        NettyClientDemo nettyClientDemo = new NettyClientDemo();
        nettyClientDemo.startClient();
    }
}
