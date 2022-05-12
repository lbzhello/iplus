package xyz.debug.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Configuration
public class ControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("================ aop start ================");
        logger.info("{}", joinPoint);

        logger.info("url: {}", getUrl(joinPoint));
        Object args = joinPoint.getArgs(); // 防止 log 将数组当成多个参数
        logger.info("args: {}", args);

        Object proceed = joinPoint.proceed();
        logger.info("result: {}", proceed);

        logger.info("================ aop end ================");
        return proceed;
    }

    private String getUrl(ProceedingJoinPoint joinPoint) {
        try {
            // 请求方法
            String httpMethod = "";
            // 请求路径
            String path = "";

            Signature signature = joinPoint.getSignature();
            Class<?> clazz = signature.getDeclaringType();

            // 类中 @RequestMapping 值
            path = concatUrl(path, Arrays.stream(MergedAnnotations.from(clazz).get(RequestMapping.class)
                    .getStringArray("path")).findAny().orElse(""));

            // 方法中 @RequestMapping 值
            Object[] args = joinPoint.getArgs();
            Class<?>[] paramTypes = Arrays.stream(args)
                    .map(Object::getClass)
                    .toArray(it -> new Class[args.length]);

            String methodName = signature.getName();
            Method method = clazz.getMethod(methodName, paramTypes);

            MergedAnnotation<RequestMapping> rmAnn = MergedAnnotations.from(method).get(RequestMapping.class);
            httpMethod = rmAnn.getValue("method", RequestMethod[].class)
                    .filter(arr -> arr.length > 0)
                    .map(arr -> arr[0].name())
                    .orElse("");

            path = concatUrl(path, Arrays.stream(rmAnn.getStringArray("path"))
                    .findAny()
                    .orElse(""));

            return httpMethod + " " + path;
        } catch (Exception e) {
            logger.error("failed to get url", e);
        }
        return "";
    }

    // 拼接完整 url: 最前面加上 '/'，最后面去掉 '/'
    private String concatUrl(String right, String left) {
        if (!right.startsWith("/")) {
            right = "/" + right;
        }

        if (right.endsWith("/")) {
            right = right.substring(0, right.length() - 1);
        }

        if (!left.startsWith("/")) {
            left = "/" + left;
        }

        if (left.endsWith("/")) {
            left = left.substring(0, left.length() - 1);
        }

        return right + left;
    }

}
