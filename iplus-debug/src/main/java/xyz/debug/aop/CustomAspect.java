package xyz.debug.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class CustomAspect {
    private static final Logger logger = LoggerFactory.getLogger(CustomAspect.class);

    @Pointcut("execution(* xyz.liujin..*(..))")
    public void pointcut() {}

    @Before("pointcut()")
    public void beforeMethod(JoinPoint joinPoint) {
        logger.info("================ aop @Before start ================");

        Signature signature = joinPoint.getSignature();
        String typeName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        logger.info("method: {}.{}", typeName, methodName);

        Object args = joinPoint.getArgs(); // 防止 log 将数组当成多个参数
        logger.info("args: {}", args);

        logger.info("================ aop @Before end ================");
    }

}
