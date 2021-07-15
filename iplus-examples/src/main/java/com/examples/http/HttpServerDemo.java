package com.examples.http;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class HttpServerDemo {
    private static Logger logger = LoggerFactory.getLogger(HttpServerDemo.class);

    public final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(10, 10,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(10),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("foo-http-pool-" + System.currentTimeMillis());
                thread.setDaemon(true);
                return thread;
            });

    private HttpServer httpServer;

    /**
     * 启动 http 服务器，监听 port 端口
     * @param port
     */
    public void start(int port) {
        try {
            // 先关闭原来的 httpServer
            stop();

            //创建一个HttpServer实例，并绑定到指定的IP地址和端口号
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);

            //创建一个HttpContext，将路径为/myserver请求映射到MyHttpHandler处理器
            httpServer.createContext("/", new HttpHandlerDemo());

            //设置服务器的线程池对象
            httpServer.setExecutor(EXECUTOR_SERVICE);

            //启动服务器
            httpServer.start();
        } catch (IOException e) {
            logger.error("failed to create ocr http server", e);
        }
    }

    public void stop() {
        if (Objects.nonNull(httpServer)) {
            httpServer.stop(1);
        }
    }
}
