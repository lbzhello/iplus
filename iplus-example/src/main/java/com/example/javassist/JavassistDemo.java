package com.example.javassist;

import javassist.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class JavassistDemo {

    /**
     * 创建类
     */
    public void createClass() {
        try {

            // CtClass 对象池
            ClassPool pool = ClassPool.getDefault();

            // 生成 java 类
            CtClass ctClassFoo = pool.makeClass("com.foo.Foo");

//            // 增加字段
//            CtField ctField = new CtField(pool.getCtClass("java.lang.String"),"name",ctClassFoo);
//            ctField.setModifiers(Modifier.PRIVATE);


            // 增加字段
            CtField ctFieldName = CtField.make("private String name;", ctClassFoo);
            ctClassFoo.addField(ctFieldName);

            // get 方法
            CtMethod ctMethodFooGetName = CtMethod.make("public String getName(){return this.name;}", ctClassFoo);
            ctClassFoo.addMethod(ctMethodFooGetName);

            // set 方法
            CtMethod ctMethodFooSetName = CtMethod.make("public void setName(String name){this.name = name;}", ctClassFoo);
            ctClassFoo.addMethod(ctMethodFooSetName);

            // 创建 bar 类
            CtClass ctClassBar = pool.makeClass("com.bar.Bar");
            CtConstructor ctConstructorBar = CtNewConstructor.make("public Bar() {}", ctClassBar);
            ctClassBar.addConstructor(ctConstructorBar);

            // getBar 方法
            CtMethod ctMethodFooGetBar = CtMethod.make("public com.bar.Bar getBar(){return new com.bar.Bar();}", ctClassFoo);
            ctClassFoo.addMethod(ctMethodFooGetBar);

            // 获取 class, 会将 class 加载至当前线程的上下文类加载器中，调用后无法修改
            Class<?> fooClass = ctClassFoo.toClass();

            Object foo = fooClass.getConstructor().newInstance();

            // 调用 setName
            MethodHandle methodHandle = MethodHandles.lookup()
                    .findVirtual(fooClass, "setName", MethodType.methodType(void.class, String.class));

            methodHandle.bindTo(foo).invoke("foooo");

            // 调用 getName
            MethodHandle methodHandleGetName = MethodHandles.lookup()
                    .findVirtual(fooClass, "getName", MethodType.methodType(String.class));

            Object rst = methodHandleGetName.bindTo(foo).invoke();
            System.out.println(rst);

            // bar 类添加 hello 方法，测试先提供类型，再修改类型
            CtClass ctClassHello = pool.get("com.examples.javassist.Hello");
            // 实现 Hello 接口
            ctClassBar.setInterfaces(new CtClass[]{ctClassHello});

            CtMethod ctMethodBarHello = CtMethod.make("public String hello(){return \"hello\";}", ctClassBar);
            ctClassBar.addMethod(ctMethodBarHello);

            // 加载当前类
            Class<?> barClass = ctClassBar.toClass();

            // 通过接口调用
            Hello bar = (Hello) fooClass.getMethod("getBar").invoke(foo);
            String hello = bar.hello();

            System.out.println(hello);

            System.out.println("end");

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改类
     */
    public void updateClass() {
        try {
            ClassPool pool = ClassPool.getDefault();

            // 添加类搜索路径
//            pool.appendClassPath("/path/to/file");
            CtClass ctClassHelloImpl = pool.get("com.examples.javassist.HelloImpl");
            CtMethod ctMethodHello = ctClassHelloImpl.getDeclaredMethod("hello");
            ctMethodHello.insertBefore("{int count = 1;System.out.println(count);}");
            // no such field: count
            // ctMethodHello.insertAfter("System.out.println(count);");

            // 测试修改后的方法
            Hello helloObj = (Hello) ctClassHelloImpl.toClass().getConstructor().newInstance();
            helloObj.hello();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 继承类
     */
    public void extendClass() {
        try {
            ClassPool pool = ClassPool.getDefault();

            CtClass ctClassHelloImpl = pool.get("com.examples.javassist.HelloImpl");
            
            // 创建一个类继承 HelloImpl
            CtClass ctClassChild = pool.makeClass("com.examples.javassist.Child");
            ctClassChild.setSuperclass(ctClassHelloImpl);

            CtMethod ctMethodHello = CtMethod.make("public String hello()" +
                    "{" +
                    "   int count = 1;" +
                    "   String rst = super.hello();" +
                    "   System.out.println(++count);" +
                    "   return rst;" +
                    "}", ctClassChild);
            ctClassChild.addMethod(ctMethodHello);

//            ctClassChild.writeFile("G:\\workspace\\iplus\\iplus-examples\\src\\main\\java");
            ctClassChild.writeFile("G:\\workspace\\iplus\\iplus-examples\\out\\production\\classes");


            Class<?> childClass = ctClassChild.toClass();
//
//            Child o = (Child) childClass.getConstructor().newInstance();
//            System.out.println(o.hello());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
