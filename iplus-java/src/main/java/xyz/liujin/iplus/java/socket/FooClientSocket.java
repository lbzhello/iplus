package xyz.liujin.iplus.java.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.CyclicBarrier;

public class FooClientSocket {
    private static final Logger logger = LoggerFactory.getLogger(FooClientSocket.class);

    private volatile boolean stop = false;

    public void startClient() {
        try {
            /**
             * 输入流和输出流必须在对流操作前获取，否则会会报错 Socket is closed
             * socket 建立后可以重用，后续可以继续调用 readLine 获取消息
             * 调用 socket 的输入流读取消息时，方法会阻塞，直至从流中收到消息
             */
            Socket socket = new Socket("127.0.0.1", FooServerSocket.SERVER_PORT);
            // 打开输出流
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            // 打开输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 同步主线程和子线程
            CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

            // 接收线程，由于 readLine 方法会阻塞调，这里单独使用线程接收
            new Thread(() -> {
                try {
                    String resp;
                    while (!stop) {
                        resp = reader.readLine();
                        logger.info(resp);
                    }
                } catch (IOException e) {
                    logger.error("failed to receive msg from server", e);
                }
            }).start();

            // 发送线程
            new Thread(() -> {
                // 发送线程
                try {
                    BufferedReader system = new BufferedReader(new InputStreamReader(System.in));
                    String msg;
                    do {
                        msg = system.readLine();
                        // 向服务器发送消息
                        writer.println(msg);
                        writer.flush();
                    } while (!Objects.equals(msg, FooServerSocket.END_STR));
                    // 关闭输入流
                    writer.close();
                    reader.close();

                    // 通知接收线程关闭
                    stop = true;

                    cyclicBarrier.await();
                } catch (Exception e) {
                    logger.error("failed to send msg to server socket");
                }
            }).start();

            // 等待发送线程执行完
            cyclicBarrier.await();

        } catch (Exception e) {
            logger.error("failed to start client socket", e);
        }
    }
}
