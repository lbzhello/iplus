package com.examples.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServerDemo {
    public void startServer() {
        // 创建两个线程组
        // bossGroup的作用是监听客户端请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // workerGroup的作用是处理每条连接的数据读写
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 服务端启动对象
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 配置线程组
        bootstrap.group(bossGroup, workerGroup)
                // 配置 IO 模型，设置服务端通道实现类型
                .channel(NioServerSocketChannel.class)
//                // 服务端 channel 配置，设置线程队列连接个数
//                .option(ChannelOption.SO_BACKLOG, 128)
//                // 客户端 channel 配置，设置保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 配置处理逻辑，每条连接的数据读写
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {

                    }
                });
        // 绑定端口
        bootstrap.bind(8888);


    }
}
