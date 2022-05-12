package xyz.debug;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class AspectTest {
    @Test
    public void adviceATest() {
        B b = new B();

        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        proxyFactory.setTarget(b);
        proxyFactory.addAspect(AspectA.class);
        proxyFactory.addAspect(AspectB.class);

        B proxyB = proxyFactory.getProxy();


        // adviceA 无法拦截，因为 b 没有精确匹配 A 类型，即 b.getClass == A.class
        // adviceB 无法拦截，因为 methodA 不是 B 自身的方法，而是父类的方法
        proxyB.methodA();

        // adviceB 可以拦截增强
        proxyB.methodB();
    }
}

class A {
    public void methodA() {
        System.out.println("i am a");
    }
}

class B extends A {
    public void methodB() {
        System.out.println("i am b");
    }
}

@Aspect
class AspectA {
    // 连接 A 类
    @Pointcut("within(A)")
    public void pa() {}

    @Before("pa()")
    public void adviceA(JoinPoint joinpoint) {
        System.out.println(joinpoint);
    }
}

@Aspect
class AspectB {
    // 连接 B 类
    @Pointcut("within(B)")
    public void pb() {}

    @Before("pb()")
    public void adviceB(JoinPoint joinpoint) {
        System.out.println(joinpoint);
    }
}
