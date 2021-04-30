package xyz.liujin.iplus.java.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * HIK所有，<br/>
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。<br/>
 *
 * @describtion
 * @copyright Copyright: 2015-2020
 * @creator liubaozhu
 * @create-time 15:23 2021/4/30
 * @department 安检业务部
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user :{修改人} :{修改时间}
 * @modify by reason :{原因}
 **/
public class FooHttpHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(FooHttpHandler.class);

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
