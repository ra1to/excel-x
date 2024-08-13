package com.raito.excelx.domain.classloader.util;

import com.raito.excelx.domain.classloader.entity.Class;
import com.raito.excelx.domain.classloader.entity.ClassWithField;
import com.raito.excelx.domain.classloader.entity.Field;
import javassist.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author raito
 * @since 2024/8/12
 */
public class ClassUtils {

    public final static String PRE_FIX = "com.raito.excelx.dynamic.";

    private final static MyClassLoader classLoader = new MyClassLoader();
    public static java.lang.Class<?> createAndLoadClass(Class clazz) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(PRE_FIX + clazz.getName());
        List<ClassWithField> classWithFields = clazz.getClassWithFields();

        for (ClassWithField classWithField : classWithFields) {
            Field field = classWithField.getField();

            CtField ctField = new CtField(pool.get(field.getType().getPackageName()), classWithField.getFieldName(), ctClass);
            ctClass.addField(ctField);
        }

        // Optionally add a default constructor
        ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));

        buildToString(ctClass, classWithFields);

        // Write class to bytecode
        byte[] byteCode = ctClass.toBytecode();

        // Load class into JVM
        java.lang.Class<?> dynamicClass = classLoader.defineClass(PRE_FIX + clazz.getName(), byteCode);
        // Optionally use reflection to instantiate and manipulate the class
        return dynamicClass;
    }

    private static void buildToString(CtClass ctClass, List<ClassWithField> classWithFields) throws CannotCompileException {
        StringBuilder sb = new StringBuilder();
        sb.append("public String toString() {")
                .append("StringBuilder sb = new StringBuilder();")
                .append("sb.append(\"").append(ctClass.getName()).append("{\");");

        for (ClassWithField classWithField : classWithFields) {
            String fieldName = classWithField.getFieldName();
            sb.append("sb.append(\"").append(fieldName).append("=\").append(").append(fieldName).append(");");
            sb.append("sb.append(\", \");");
        }
        // Remove the trailing comma and space
        sb.append("if (sb.length() > ").append("0) sb.setLength(sb.length() - 2);");
        sb.append("sb.append(\"}\");")
                .append("return sb.toString();")
                .append("}");

        CtMethod ctMethod = CtNewMethod.make(sb.toString(), ctClass);
        ctClass.addMethod(ctMethod);
    }

    public static java.lang.Class<?> getClass(String name) throws ClassNotFoundException {

        return classLoader.findClass(name);
    }

    private static class MyClassLoader extends ClassLoader {
        private static final Map<String, java.lang.Class<?>> classCache = new HashMap<>();

        @Override
        public java.lang.Class<?> findClass(String name) throws ClassNotFoundException {
            java.lang.Class<?> cachedClass = classCache.get(name);
            if (cachedClass != null) {
                return cachedClass;
            }
            return super.findClass(name);
        }

        public java.lang.Class<?> defineClass(String name, byte[] b) {
            java.lang.Class<?> definedClass = super.defineClass(name, b, 0, b.length);
            classCache.put(name, definedClass);
            return definedClass;
        }
    }
}