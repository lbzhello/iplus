package com.examples.nio.channel;

import com.examples.nio.buffer.Buffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class SocketChannelDemo {
    private static final Logger logger = LoggerFactory.getLogger(SocketChannelDemo.class);

    public void startClientSocket() {
        Selector selector = null;

        try {
            SocketChannel sc = SocketChannel.open();
            // 设置为非阻塞
            sc.configureBlocking(false);

            selector = Selector.open();

            // 向选择器注册通道，关注读写事件
            sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, Buffers.allocate(256, 256));

            // 连接服务器
            sc.connect(new InetSocketAddress(ServerSocketChannelDemo.PORT));

            // 由于是非阻塞，连接可能还没有创建完成
            // 等待三次握手完成
            while (!sc.finishConnect()) {
                ;
            }

            logger.info("connect server socket success, port {}", ServerSocketChannelDemo.PORT);
        } catch (IOException e) {
            logger.error("failed to connect server socket", e);
        }

        try {
            boolean stop = false;
            while (!stop) {
                if (Objects.isNull(selector)) {
                    return;
                }

                // 阻塞等待
                selector.select();


                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                // 遍历准备好的通道
                while (it.hasNext()) {
                    SelectionKey key = it.next();

                    // 防止下次 select 方法返回已处理的通道
                    it.remove();

                    // 获取 sc 缓冲区
                    Buffers buffers = (Buffers) key.attachment();
                    ByteBuffer rbb = buffers.getReadBuffer();
                    ByteBuffer wbb = buffers.gerWriteBuffer();

                    // 获取 sc
                    SocketChannel sc = (SocketChannel) key.channel();

                    if (key.isReadable()) {
                        sc.read(rbb);
                        rbb.flip();

                        CharBuffer cb = StandardCharsets.UTF_8.decode(rbb);
                        rbb.clear();

                        logger.info("read msg from server socket: {}", cb.toString());

                        // 只和服务器交互一次
                        logger.info("stop client socket");
                        stop = true;
                        break;
                    }

                    if (key.isWritable()) {
                        String msg = "I'm groot";
                        wbb.put(msg.getBytes(StandardCharsets.UTF_8));
                        wbb.flip();

                        sc.write(wbb);
                        wbb.clear();

                        logger.info("write msg to server socket: {}", msg);

                        // 若底层缓冲区有空闲空间，写操作一直是就绪的，这里写完后去掉，需要再写
                        key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                    }

                }
            }
        } catch (Exception e) {
            logger.error("client socket exception", e);
        } finally {
            if (Objects.nonNull(selector)) {
                try {
                    selector.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
