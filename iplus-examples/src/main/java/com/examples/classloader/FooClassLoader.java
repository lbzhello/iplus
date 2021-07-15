package com.examples.classloader;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.nio.file.Path;
import java.util.Iterator;

public class FooClassLoader {
    public static void main(String[] args) {
        String javaName = "";
        String javaSrc = "";
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manager = javaCompiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> javaFileObjects = manager.getJavaFileObjects(Path.of(""));
        Iterator<? extends JavaFileObject> iterator = javaFileObjects.iterator();
        if (iterator.hasNext()) {
            JavaFileObject javaFileObject = iterator.next();
//            javaCompiler.getTask()
        }

    }
}
