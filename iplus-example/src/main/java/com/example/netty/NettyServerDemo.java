package com.example.netty;

import com.example.util.ConcurrentUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CyclicBarrier;

public class NettyServerDemo {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerDemo.class);

    public void startServer() {
        // 防止服务器退出
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

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
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            // 当前channel从远端读取到数据时触发
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 接收到的消息
                                ByteBuf rbb = (ByteBuf) msg;
                                String rmsg = rbb.toString(StandardCharsets.UTF_8);
                                logger.info("[inbound channelRead] server received msg: {}", rmsg);

                                // 发送消息
                                String smsg = "I'm server";
                                ByteBuf wbb = ctx.alloc().buffer();
                                wbb.writeBytes(smsg.getBytes(StandardCharsets.UTF_8));

                                ctx.channel().writeAndFlush(wbb);

                                logger.info("[inbound channelRead] server send msg: {}", smsg);
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                logger.error("exception", cause);
                            }
                        });

//                        ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
//                            @Override
//                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                                ByteBuf rbb = (ByteBuf) msg;
//                                String rmsg = rbb.toString(StandardCharsets.UTF_8);
//                                logger.info("outbound server received msg: {}", rmsg);
//
//                                // 发送消息
//                                String smsg = "I'm server, your msg " + rmsg;
//
//                                ByteBuf wbb = ctx.alloc().buffer();
//                                wbb.writeBytes(smsg.getBytes(StandardCharsets.UTF_8));
//
//                                ctx.channel().writeAndFlush(wbb);
//
//                                logger.info("outbound server send msg: {}", smsg);
//                            }
//                        });
                    }
                });
        // 绑定端口
        bootstrap.bind(8888);

        ConcurrentUtils.barrierAwait(cyclicBarrier);
    }
}
