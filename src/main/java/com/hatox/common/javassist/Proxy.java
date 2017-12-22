package com.hatox.common.javassist;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Proxy {
    private final static AtomicLong atomicLong=new AtomicLong(0);
    public static  Proxy getProxy(Class<?>... classes){
       return  getProxy(Proxy.class.getClassLoader(),classes);
    }
    public  static Proxy getProxy(ClassLoader classLoader, Class<?>... classes){
        for(Class clazz:classes) {
            if (!clazz.isInterface()) {
                throw new IllegalStateException(clazz.getName() + "is not interface");
            }
        }

        try {

            List<Method> methods=new ArrayList<Method>();
            ClassGenerator cpp=ClassGenerator.getInstance(classLoader);
            for(Class clazz:classes) {
                cpp.setmInterface(clazz.getName());
                for (Method method : clazz.getMethods()) {
                    StringBuilder code=new StringBuilder();
                    Class rt = method.getReturnType();
                    Class[] pts = method.getParameterTypes();
                    Class[] ets = method.getExceptionTypes();
                    String methodName = method.getName();
                    code.append("{");
                    code.append("Object[] args=new Object[").append(pts.length).append("];");
                    for (int i = 0; i < pts.length; i++) {
                        code.append("args[").append(i).append("]=").append("($w)$").append(i + 1).append(";");
                    }
                    code.append("Object ret=h.invoke(this,methods[").append(methods.size()).append("],args);");
                    if (!Void.TYPE.equals(rt)) {
                        code.append("return (").append(rt.getName()).append(")ret;");
                    }
                    code.append("}");
                    cpp.addMethod(methodName, rt, ets, pts, code);
                    methods.add(method);
                }
            }
            String className=PROXY_PKG+".Proxy"+atomicLong.getAndIncrement();
            cpp.setClassName(className);
            cpp.addDefaultConstrust();
            cpp.addField("private java.lang.reflect.InvocationHandler h;");
            cpp.addField("public static java.lang.reflect.Method[] methods;");
            cpp.addConstructor("public <init>(java.lang.reflect.InvocationHandler arg){this.h=$1;}");
            Class cppClass=cpp.toClass();

            cppClass.getField("methods").
                    set(null,methods.toArray(new Method[0]));
//            Field field=cppClass.getField("methods");
            Object instance=cppClass.newInstance();
            String superProxy=PROXY_PKG+".proxy"+atomicLong.getAndIncrement();
            ClassGenerator csp=ClassGenerator.getInstance(classLoader);
            csp.setClassName(superProxy);
            csp.addDefaultConstrust();
            csp.setSuperClassName(Proxy.class.getName());
            csp.addMethod("public Object newInstance(java.lang.reflect.InvocationHandler h){return new "+className+"($1);}");
            return (Proxy) csp.toClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public abstract Object newInstance(InvocationHandler handler);
    private final static String  PROXY_PKG=Proxy.class.getPackage().getName();
}
