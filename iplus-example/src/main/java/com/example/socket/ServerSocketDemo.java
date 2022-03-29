package com.example.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocketDemo {
    private static final Logger logger = LoggerFactory.getLogger(ServerSocketDemo.class);

    public static final int SERVER_PORT = 8888;

    // 输入 q 停止客户端连接
    public static final String END_STR = "q";

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

            while (true) {
                // 阻塞直到收到客户端消息
                try {
                    Socket socket = serverSocket.accept();
                    // 启动线程处理客户端消息
                    executorService.execute(() -> {
                        try {
                            /**
                             * 输入流和输出流必须在对流操作前获取，否则会会报错 Socket is closed
                             * socket 建立后可以重用，后续可以继续调用 readLine 获取消息
                             * 调用 socket 的输入流读取消息时，方法会阻塞，直至从流中收到消息
                             */
                            // 打开输入流
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                 // 打开输出流
                                 PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())))) {

                                // 接收客户端消息
                                String msg;
                                do {
                                    msg = reader.readLine();
                                    logger.info(msg);

                                    // 向客户端发送消息
                                    writer.println("hello, I'm server. I received your message: " + msg);
                                    writer.flush();
                                } while (!Objects.equals(msg, END_STR));

                                logger.info("client socket closed");
                            }

                            socket.close();
                        } catch (IOException e) {
                            logger.debug("failed to deal socket msg", e);
                        } finally {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                // ignore
                            }
                        }
                    });
                } catch (IOException e) {
                    logger.error("exception occur when accept socket", e);
                }

            }
        } catch (IOException e) {
            logger.error("failed to start server socket", e);
        }
    }
}
