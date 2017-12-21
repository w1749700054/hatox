package com.hatox.common.javassist;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Proxy {
    InvocationHandler h;
    private final static AtomicLong atomicLong=new AtomicLong(0);
    public static  Proxy getProxy(Class clazz){
       return  getProxy(clazz,Proxy.class.getClassLoader());
    }
    public  static Proxy getProxy(Class<?> clazz,ClassLoader classLoader){
        if(!clazz.isInterface()){
            throw new IllegalStateException(clazz.getName()+"is not interface");
        }
        try {
            StringBuilder code=new StringBuilder();
            List<Method> methods=new ArrayList<Method>();
            ClassGenerator cpp=ClassGenerator.getInstance(classLoader);

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
                code.append("Object ret=h.invoke(this,methods[").append(methods.size()).append("],args);");
                if(!Void.TYPE.equals(rt)){
                    code.append("return ret;");
                }
                code.append("}");
                cpp.addMethod(methodName,rt,ets,pts,code);
            }
            String className=PROXY_PKG+".Proxy"+atomicLong.getAndIncrement();
            cpp.setClassName(className);
            cpp.addDefaultConstrust();
            cpp.setmInterface(clazz.getName());
            cpp.addField("private java.lang.reflect.InvocationHandler h;");
            cpp.addField("public static java.lang.reflect.Method[] methods;");
            cpp.addConstructor("public <init>(java.lang.reflect.InvocationHandler arg){this.h=$1;}");
            Class cppClass=cpp.toClass();
            Field[] fields=cppClass.getFields();
            cppClass.getField("methods").
                    set(null,methods.toArray(new Method[0]));
            String superProxy=PROXY_PKG+".proxy"+atomicLong.getAndIncrement();
            ClassGenerator csp=ClassGenerator.getInstance(classLoader);
            csp.addDefaultConstrust();
            csp.setSuperClassName(Proxy.class.getName());
            csp.addMethod("public Object newInstance(java.lang.reflect.Invocation handler){return new "+className+"($1);");
            return (Proxy) csp.toClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public abstract Object newInstance(InvocationHandler handler);
    private final static String  PROXY_PKG=Proxy.class.getPackage().getName();
}
