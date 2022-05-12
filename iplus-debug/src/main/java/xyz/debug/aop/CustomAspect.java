package xyz.debug.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class CustomAspect {
    private static final Logger logger = LoggerFactory.getLogger(CustomAspect.class);

    @Pointcut("execution(* xyz.liujin..*(..)) && !@within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("================ aop start ================");
        logger.info("{}", joinPoint);

        Object args = joinPoint.getArgs(); // 防止 log 将数组当成多个参数
        logger.info("args: {}", args);

        Object proceed = joinPoint.proceed();
        logger.info("result: {}", proceed);

        logger.info("================ aop end ================");
        return proceed;
    }

}
