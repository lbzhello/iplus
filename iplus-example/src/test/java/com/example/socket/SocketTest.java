package com.example.socket;

import org.junit.jupiter.api.Test;

public class SocketTest {
    @Test
    public void startServerSocket() {
        ServerSocketDemo serverSocketDemo = new ServerSocketDemo();
        serverSocketDemo.startServer();
    }

    @Test
    public void startClientSocket() {
        ClientSocketDemo clientSocket = new ClientSocketDemo();
        clientSocket.startClient();
    }
}
