package com.examples.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @since  15:23 2021/4/30
 **/
public class HttpHandlerDemo implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(HttpHandlerDemo.class);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            // 获取请求方法和请求路径
            String httpMethod = httpExchange.getRequestMethod();
            URI uri = httpExchange.getRequestURI();
            logger.debug("receive http request from  {} {}", httpMethod, uri);

            // 获取请求体
            InputStream requestBody = httpExchange.getRequestBody();
            String body = StreamUtils.copyToString(requestBody, StandardCharsets.UTF_8);

            logger.debug("receive http msg {}", body);

            // 设置响应头，必须在sendResponseHeaders方法之前设置
            httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=UTF-8");
            // 设置响应码和响应体长度，必须在getResponseBody方法之前调用
            httpExchange.sendResponseHeaders(200, 0);
            // 写消息
            PrintWriter out = new PrintWriter(httpExchange.getResponseBody());
            out.println("hello world!");
            out.println("received: " + body);

            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("failed to handle http request", e);
        }
    }
}
