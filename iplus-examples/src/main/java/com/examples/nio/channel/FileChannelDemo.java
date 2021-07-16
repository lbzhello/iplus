package com.examples.nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FileChannel read write 方法都是阻塞的
 * 好像没有 PrintWriter 好用
 */
public class FileChannelDemo {
    public void readFile() {
        try {
            Path path = Paths.get("G:/tmp.txt");
            // 打开 channel
            FileChannel fc = FileChannel.open(path);
            // 创建一个 buffer 用于存放数据
            ByteBuffer bb = ByteBuffer.allocate(128);

            // 读取 channel 数据写到 buffer
            fc.read(bb);
            // 转成读模式
            bb.flip();

            Charset charset = StandardCharsets.UTF_8;
            CharBuffer cb = charset.decode(bb);

            System.out.println(cb.toString());


            bb.clear();
            fc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFile() {
        try {
            Path path = Paths.get("G:/tmp.txt");
            if (!path.toFile().exists()) {
                Files.createFile(path);
            }

            FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
            // 打开一个channel
            FileChannel fileChannel = fileOutputStream.getChannel();
            // 创建一个 buffer 用于存放数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(64);
            byteBuffer.put("hello world file channel\n".getBytes());

            // 转成读模式，limit 被设置成 position
            byteBuffer.flip();
            // 从 buffer 中读数据写入 channel
            fileChannel.write(byteBuffer);

            // 清空 buffer, 转成写模式
            byteBuffer.clear();

            byteBuffer.put("another line\n".getBytes());

            byteBuffer.flip();

            fileChannel.write(byteBuffer);

            byteBuffer.clear();

            fileChannel.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
