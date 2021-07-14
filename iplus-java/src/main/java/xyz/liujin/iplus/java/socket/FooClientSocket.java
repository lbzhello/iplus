package xyz.liujin.iplus.java.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class FooClientSocket {
    private static final Logger logger = LoggerFactory.getLogger(FooClientSocket.class);

    public void startClient() {
        try (Socket socket = new Socket("127.0.0.1", FooServerSocket.SERVER_PORT)) {
            /**
             * 输入流和输出流必须在对流操作前获取，否则会会报错 Socket is closed
             */
            // 打开输出流
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                 // 打开输入流
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // 向服务器发送消息
                writer.println("hello, I'm from client");
                writer.flush();

                // 接收服务器消息
                String msg = reader.readLine();
                logger.info(msg);
            }

        } catch (IOException e) {
            logger.error("failed to start client socket", e);
        }
    }
}
