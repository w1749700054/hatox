package com.hatox.common.javassist;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Proxy {
    InvocationHandler h;
    private final static AtomicLong atomicLong=new AtomicLong(0);
    public static  void getProxy(Class clazz){
        getProxy(clazz,Proxy.class.getClassLoader());
    }
    public  static void getProxy(Class<?> clazz,ClassLoader classLoader){
        if(!clazz.isInterface()){
            throw new IllegalStateException(clazz.getName()+"is not interface");
        }
        StringBuilder code=new StringBuilder();
        List<Method> methods=new ArrayList<Method>();
        ClassGenerator cpp=ClassGenerator.getInstance(classLoader);
        String className=PROXY_PKG+".Proxy"+atomicLong.getAndIncrement();
        for(Method method:clazz.getMethods()){
            Class rt=method.getReturnType();
            Class[] pts=method.getParameterTypes();
            Class[] ets=method.getExceptionTypes();
            String methodName=method.getName();
            code.append("{");
            code.append("Object[] args=new Object[").append(pts.length).append("];");
            for(int i=0;i<pts.length;i++){
                code.append("args[").append(i).append("]=").append("$(w)$(").append(i+1).append(");");
            }
            code.append("Object ret=h.invoke(this,methods[").append(methods.size()).append("],args;");
            if(!Void.TYPE.equals(rt)){
                code.append("return ret;");
            }
            code.append("}");
            cpp.addMethod(methodName,rt,ets,pts,code);
        }
        cpp.setClassName(className);
        cpp.addDefaultConstrust();
        cpp.setmInterface(clazz.getName());
        cpp.addField("private java.lang.reflect.InvocationHandler h");
        cpp.addConstructor("public "+className+"(java.lang.reflect.InvocationHandler h){ this.h=h;}");
        cpp.addField("Method[] methods");


    }
    public abstract Object newInstance(InvocationHandler handler);
    private final static String  PROXY_PKG=Proxy.class.getPackage().getName();
}
