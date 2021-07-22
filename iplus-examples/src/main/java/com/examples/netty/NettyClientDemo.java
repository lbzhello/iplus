package com.examples.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NettyClientDemo {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientDemo.class);

    public void startClient() {
        // 线程组,处理每条连接的数据读写
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        // 客户端启动类
        Bootstrap bootstrap = new Bootstrap();

        // 配置线程组
        bootstrap.group(workerGroup)
                // 配置 IO 模型，设置服务端通道实现类型
                .channel(NioSocketChannel.class)
                // IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                    }
                });

        // 建立连接
        bootstrap.connect("127.0.0.1", 8888).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("连接成功");
            } else {
                logger.info("连接失败");
                // 重试连接
            }
        });

    }

    /**
     * 连接服务器，带有失败重试机制
     * @param ip
     * @param port
     * @param retry
     */
    public void connect(Bootstrap bootstrap, String ip, int port, int retry) {
        bootstrap.connect(ip, port).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("连接成功");
            } else if (retry == 0) {
                logger.info("重试次数已经用完，放弃连接");
            } else {
                logger.info("重试 {}", retry);
                int nextRetry = retry - 1;
                bootstrap.config().group().schedule(() -> connect(bootstrap, ip, port, nextRetry), 2, TimeUnit.SECONDS);
            }
        });
    }
}
