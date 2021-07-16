package com.examples.nio.channel;

import org.junit.jupiter.api.Test;

public class ServerSocketChannelTest {
    @Test
    public void startServerSocket() {
        ServerSocketChannelDemo serverSocketChannelDemo = new ServerSocketChannelDemo();
        serverSocketChannelDemo.startServerSocket();
    }

    @Test
    public void startClientSocket() {
        SocketChannelDemo socketChannelDemo = new SocketChannelDemo();
        socketChannelDemo.startClientSocket();
    }
}
