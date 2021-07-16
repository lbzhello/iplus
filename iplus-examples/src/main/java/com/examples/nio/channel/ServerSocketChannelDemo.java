package com.examples.nio.channel;

import com.examples.nio.buffer.Buffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class ServerSocketChannelDemo {
    private static final Logger logger = LoggerFactory.getLogger(ServerSocketChannelDemo.class);
    
    public static final int PORT = 8888;

    public void startServerSocket() {
        try {

            ServerSocketChannel ssc = ServerSocketChannel.open();
            // 配置为非阻塞，默认阻塞
            ssc.configureBlocking(false);

            // 绑定端口，最大连接数 100
            ssc.bind(new InetSocketAddress(PORT), 100);

            Selector selector = Selector.open();

            // 将 channel 注册进 selector; 只对 tcp 连接事件感兴趣
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("ServerSocketChannel started");

            while (true) {

                // 获取 socket 事件
                int n = selector.select();
                if (n == 0) {
                    continue;
                }

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> it = selectionKeys.iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();

                    // 防止下次 select 方法返回已处理过的通道
                    it.remove();

                    try {
                        // ssc 只对连接事件感兴趣
                        if (key.isAcceptable()) {
                            logger.info("key is acceptable");
                            // 每个通道在内核中都对应一个socket缓冲区
                            SocketChannel sc = ssc.accept();
                            // 配置为非阻塞，默认阻塞
                            sc.configureBlocking(false);

                            // sc 注册进 selector, 关心读事件
                            sc.register(selector, SelectionKey.OP_READ, Buffers.allocate(256, 256));

                            logger.info("wait to read");
                        }

                        // sc 关心读事件
                        if (key.isReadable()) {
                            logger.info("key is readable");
                            // 获取通道对应的缓冲区
                            Buffers buffers = (Buffers) key.attachment();
                            ByteBuffer rbb = buffers.getReadBuffer();
                            ByteBuffer wbb = buffers.gerWriteBuffer();

                            // 获取 sc
                            SocketChannel sc = (SocketChannel) key.channel();

                            // 读取 sc 传来的数据
                            sc.read(rbb);
                            // 转成读模式, limit = position; position = 0
                            rbb.flip();
                            CharBuffer cb = StandardCharsets.UTF_8.decode(rbb);
                            logger.info("received msg: " + cb.toString());

                            // 准备好向客户端发送的消息，后面 key.isWritable() 时，可直接写入
                            wbb.put("I'm server, received: ".getBytes(StandardCharsets.UTF_8));
                            rbb.rewind(); // 重新读
                            wbb.put(rbb);

                            // 清空读缓存，下次可直接写入
                            rbb.clear();

                            // 增加关心写事件，（sc 前面已经注册进 selector）
                            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);

                            logger.info("wait to write");
                        }

                        // sc 关心写事件
                        if (key.isWritable()) {
                            logger.info("key is writable");

                            Buffers buffers = (Buffers) key.attachment();
                            ByteBuffer wbb = buffers.gerWriteBuffer();
                            wbb.flip();

                            SocketChannel sc = (SocketChannel) key.channel();

                            int len = 0;
                            while (wbb.hasRemaining()) {
                                len = sc.write(wbb);
                                // 说明底层 socket 写缓冲已满(wbb 有数据，但是写入为 0)
                                if (len == 0) {
                                    break;
                                }
                            }
                            // 防止数据还有剩余
                            wbb.compact();

                            // 说明数据全部写入到底层的socket写缓冲区
                            if (len != 0) {
                                logger.info("write success, cancel write event");

                                // 取消通道写事件
                                key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();
                        key.channel().close();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }


    }
}
