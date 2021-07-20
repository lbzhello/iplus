package com.examples.aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * aio 读写文件示例
 */
public class AsynchronousFileChannelDemo {
    private static final Logger logger = LoggerFactory.getLogger(AsynchronousFileChannelDemo.class);

    public void readFile(String path) {
        try {
            // 打开文件 channel
            AsynchronousFileChannel afc = AsynchronousFileChannel.open(Path.of(path), StandardOpenOption.READ);
            ByteBuffer rbb = ByteBuffer.allocate(128);

            /** 异步读取文件，读取完成后回调 **/
            afc.read(rbb, 0, rbb, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    // 转成读模式
                    attachment.flip();
                    String msg = StandardCharsets.UTF_8.decode(attachment).toString();
                    logger.info("read file content: {}", msg);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    logger.error("failed to read file", exc);
                }
            });
        } catch (Exception e) {
            logger.error("open file failed", e);
        }
    }

    public void writeFile(String path) {
        try {
            // 打开文件 channel
            AsynchronousFileChannel afc = AsynchronousFileChannel.open(Path.of(path), StandardOpenOption.WRITE);
            ByteBuffer wbb = ByteBuffer.allocate(128);
            wbb.put("I'm groot".getBytes(StandardCharsets.UTF_8));
            // 转成读模式
            wbb.flip();
            /** 异步写入文件，写入完成后回调 **/
            afc.write(wbb, 0, wbb, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    logger.info("success write content to file, len {}", result);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    logger.error("failed to write file", exc);
                }
            });
        } catch (IOException e) {
            logger.debug("open file failed", e);
        }
    }
}
