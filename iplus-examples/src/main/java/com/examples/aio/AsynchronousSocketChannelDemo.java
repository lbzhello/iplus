package com.examples.aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * aio client 端示例
 */
public class AsynchronousSocketChannelDemo {
    private static final Logger logger = LoggerFactory.getLogger(AsynchronousSocketChannelDemo.class);

    public void startAsynchronousSocket() {
        try {
            // 客户端等待子线程收到服务器响应后退出
            CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

            // 异步线程池，用于处理 IO 事件，派发 CompletionHandler
            AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withThreadPool(Executors.newCachedThreadPool());
            // 打开异步通道
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            /** 异步连接服务器，连接完成后回调 **/
            socketChannel.connect(new InetSocketAddress("127.0.0.1", AsynchronousServerSocketChannelDemo.PORT), null, new CompletionHandler<Void, Object>() {
                @Override
                public void completed(Void result, Object attachment) {
                    ByteBuffer wbb = ByteBuffer.allocate(1024);
                    wbb.put("I'm groot".getBytes(StandardCharsets.UTF_8));
                    // 转成读模式
                    wbb.flip();
                    /** 异步写入发送的数据，写入完成后回调 **/
                    // wbb 数据写入 channel
                    socketChannel.write(wbb, wbb, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            // 没写完则继续写
                            if (attachment.hasRemaining()) {
                                socketChannel.write(attachment, attachment, this);
                            } else { // 读取服务器响应
                                ByteBuffer rbb = ByteBuffer.allocate(1024);
                                /** 异步读取服务器响应数据，读取完成后回调 **/
                                socketChannel.read(rbb, rbb, new CompletionHandler<Integer, ByteBuffer>() {
                                    @Override
                                    public void completed(Integer result, ByteBuffer attachment) {
                                        // 转成读模式
                                        attachment.flip();
                                        String msg = StandardCharsets.UTF_8.decode(rbb).toString();
                                        logger.info("received from server: {}", msg);

                                        try {
                                            // 收到响应后退出
                                            cyclicBarrier.await();
                                        } catch (Exception e) {
                                            // ignore
                                        }
                                    }

                                    @Override
                                    public void failed(Throwable exc, ByteBuffer attachment) {
                                        logger.info("client socket read failed", exc);

                                        try {
                                            socketChannel.close();
                                        } catch (IOException e) {
                                            // ignore
                                        }

                                        try {
                                            // 退出客户端
                                            cyclicBarrier.await();
                                        } catch (Exception e) {
                                            // ignore
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            logger.info("client socket write failed", exc);
                            try {
                                // 退出客户端
                                cyclicBarrier.await();
                            } catch (Exception e) {
                                // ignore
                            }
                        }

                    });
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    logger.info("client socket connect failed", exc);
                    try {
                        // 退出客户端
                        cyclicBarrier.await();
                    } catch (Exception e) {
                        // ignore
                    }
                }
            });

            cyclicBarrier.await();
            logger.info("client socket stopped");
        } catch (Exception e) {
            logger.error("startAsynchronousSocket", e);
        }

    }
}
