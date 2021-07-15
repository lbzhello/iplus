package com.examples.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class ServerSocketChannelDemo {
    public void startServer() {
        try {

            ServerSocketChannel ssc = ServerSocketChannel.open();
            // 配置为非阻塞，默认阻塞
            ssc.configureBlocking(false);

            // 绑定端口，最大连接数 100
            ssc.bind(new InetSocketAddress(8888), 100);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
