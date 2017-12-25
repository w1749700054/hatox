package com.hatox.common.javassist;

import com.sun.org.apache.bcel.internal.generic.ClassGen;
import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class ClassGenerator {
    private final static Map<ClassLoader, ClassPool> MAP_POOL=new ConcurrentHashMap<ClassLoader, ClassPool>();
    private static final String SIMPLE_NAME_TAG = "<init>";
    private ClassPool mPool;
    private CtClass ctClass;
    private String superClassName;
    private String className;
    private List<String> mInterfaces;
    private Map<String,Method> mMethodMap=new ConcurrentHashMap<String, Method>();
    private List<String> mFields,mConstructors,mMethods;
    private boolean hasDefaultConstrust=false;
    private ClassGenerator(ClassLoader classLoader){
        mPool=MAP_POOL.get(classLoader);
         if(mPool==null){
             mPool=ClassPool.getDefault();
             MAP_POOL.put(classLoader,mPool);
         }
    }
    public Class toClass() throws Exception {
        return toClass(ClassGenerator.class.getClassLoader(),getClass().getProtectionDomain());
    }

    public Class toClass(ClassLoader classLoader, ProtectionDomain pd) throws NotFoundException, CannotCompileException {
        if(ctClass!=null){
            ctClass.detach();
        }
        ctClass=mPool.makeClass(className);
        if(superClassName!=null){
            CtClass cc=mPool.get(superClassName);
            ctClass.setSuperclass(cc);
        }
        ctClass.addInterface(mPool.get(DefaultInterface.class.getName()));
       if(mInterfaces!=null&&mInterfaces.size()>0){
            for(String mInterface:mInterfaces){
                ctClass.addInterface(mPool.get(mInterface));
            }
       }
       if(mFields!=null) {
           for (String field : mFields) {
               ctClass.addField(CtField.make(field, ctClass));
           }
       }
        if (hasDefaultConstrust){
            ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));
        }
        if(mConstructors!=null) {
            for (String s : mConstructors) {
                ctClass.addConstructor(CtNewConstructor.make(s.replaceFirst("<init>", className.substring(className.lastIndexOf(".") + 1, className.length())), ctClass));
            }
        }
        if(mMethods!=null) {
            for (String method : mMethods) {
                ctClass.addMethod(CtMethod.make(method, ctClass));
            }
        }
        return ctClass.toClass(classLoader,pd);
    }

    public static ClassGenerator getInstance(ClassLoader classLoader){
        if(classLoader==null){
            classLoader=ClassGenerator.class.getClassLoader();
        }
        return new ClassGenerator(classLoader);
    }
    public ClassGenerator addField(String field){
        if(mFields==null){
            mFields=new ArrayList<String>();
        }
        mFields.add(field);
        return this;
    }
    public ClassGenerator addConstructor(String constructor){
        if(mConstructors==null){
            mConstructors=new ArrayList<String>();
        }
        mConstructors.add(constructor);
        return this;
    }
    public ClassGenerator addMethod(String method){
        if(mMethods==null){
            mMethods=new ArrayList<String>();
        }
        mMethods.add(method);
        return this;
    }
    public ClassGenerator addDefaultConstrust(){
        hasDefaultConstrust=true;
        return this;
    }
    public ClassGenerator setmInterface(String mInterface){
        if(mInterfaces==null){
            mInterfaces=new ArrayList();
        }
        mInterfaces.add(mInterface);
        return this;
    }

    public ClassGenerator setClassName(String className) {
        this.className = className;
        return this;
    }

    public ClassGenerator setSuperClassName(String superClassName) {
        this.superClassName = superClassName;
        return this;
    }

    public void addMethod(String methodName, Class rt, Class[] ets, Class[] pts, StringBuilder code) {
        StringBuilder sb=new StringBuilder();
        sb.append("public ").append(rt.getName()).append(" ").append(methodName).append(" (");
        for(int i=0;i<pts.length;i++){
            if(i>0){
                sb.append(",");
            }
            sb.append(pts[i].getName()).append(" arg").append(i+1);
        }
        sb.append(")");
        if(ets!=null&&ets.length>0){
            sb.append("throws ");
            for(int i=0;i<ets.length;i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(ets[i].getName());
            }
        }
        sb.append(code);
        addMethod(sb.toString());
    }
    public static boolean isDynamicClass(Class<?> c){
        return DefaultInterface.class.isAssignableFrom(c);
    }
    public static interface DefaultInterface{

    }
}
