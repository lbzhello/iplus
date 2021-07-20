package com.examples.aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * aio server 端示例
 */
public class AsynchronousServerSocketChannelDemo {
    private static final Logger logger = LoggerFactory.getLogger(AsynchronousServerSocketChannelDemo.class);
    public static final int PORT = 8888;

    public void startAsynchronousServerSocket() {
        try {
            CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
            // 异步线程池，用于处理 IO 事件，派发 CompletionHandler
            AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withThreadPool(Executors.newCachedThreadPool());

            // 打开 channel
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(asynchronousChannelGroup);
            serverSocketChannel.bind(new InetSocketAddress(PORT));

            /** 异步接收请求，连接完成后回调 **/
            serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                @Override
                public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
                    // 继续接收下一个连接事件，否则一次 accept 后就退出了，类似 NIO 中的 while(true)
                    serverSocketChannel.accept(attachment, this);

                    ByteBuffer rbb = ByteBuffer.allocate(1024);
                    /** 异步读取数据, 数据读取完成后回调 **/
                    // 系统会将数据读取到 buffer （第一个参数）
                    socketChannel.read(rbb, rbb, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) { //
                            attachment.flip();
                            String msg = StandardCharsets.UTF_8.decode(attachment).toString();
                            logger.info("received client msg: {}", msg);

                            byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
                            ByteBuffer wbb = ByteBuffer.allocate(1024);
                            wbb.put("hello ".getBytes(StandardCharsets.UTF_8));
                            wbb.put(bytes);
                            wbb.flip();
                            /** 异步写数据，数据写入完成后回调 **/
                            // 系统会将 rbb 中的数据写入 socket
                            socketChannel.write(wbb, wbb, new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    // 如果没有发送完成就继续发送
                                    if (attachment.hasRemaining()) {
                                        socketChannel.write(attachment, attachment, this);
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    logger.error("server socket write failed", exc);
                                }
                            });
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            logger.error("server socket read failed", exc);
                        }
                    });
                }


                @Override
                public void failed(Throwable exc, Object attachment) {
                    logger.error("server socket accept failed", exc);
                }
            });

            // 防止服务器推出
            cyclicBarrier.await();

            logger.info("server socket stopped");
        } catch (Exception e) {
            logger.error("server socket error", e);
        }
    }
}
