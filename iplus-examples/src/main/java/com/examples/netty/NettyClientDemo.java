package com.examples.netty;

import com.examples.util.ConcurrentUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class NettyClientDemo {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientDemo.class);

    public void startClient() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

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

                        // 客户端与服务器建立连接后调用
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            // 当前channel激活的时候触发
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                String msg = "I am groot";
                                ctx.writeAndFlush(Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8));
//                                ctx.writeAndFlush(Unpooled.copiedBuffer("hhhh", StandardCharsets.UTF_8));

                                logger.info("[inbound channelActive] client send msg: {}", msg);

                            }

                            // 当前channel从远端读取到数据时触发
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf bb = (ByteBuf) msg;
                                logger.info("[inbound channelRead] client received msg: {}", bb.toString(StandardCharsets.UTF_8));

                                ConcurrentUtils.barrierAwait(cyclicBarrier);
                            }

                        });

                    }
                });

        // 建立连接
        bootstrap.connect("127.0.0.1", 8888).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("connect success");
            } else {
                logger.info("connect failed");
                // 重试连接
            }
        });

        ConcurrentUtils.barrierAwait(cyclicBarrier);
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
