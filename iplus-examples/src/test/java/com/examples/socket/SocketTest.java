package com.examples.socket;

import org.junit.jupiter.api.Test;

public class SocketTest {
    @Test
    public void startServerSocket() {
        FooServerSocket fooServerSocket = new FooServerSocket();
        fooServerSocket.startServer();
    }

    @Test
    public void startClientSocket() {
        FooClientSocket clientSocket = new FooClientSocket();
        clientSocket.startClient();
    }
}
