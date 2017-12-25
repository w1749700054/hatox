package com.hatox.common.javassist;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class Wrap {
    public static Wrap getWrap(Class cls){
        while(ClassGenerator.isDynamicClass(cls))
            cls.getSuperclass();
        if(cls.equals(Object.class))return null;
        ClassLoader cl=cls.getClassLoader();
        String className=cls.getName();
        ClassGenerator ccp=ClassGenerator.getInstance(cl);
        StringBuilder svCode=new StringBuilder();
        StringBuilder gvCode=new StringBuilder();
        StringBuilder ivCode=new StringBuilder();
        gvCode.append("public Object getPropertyValue(Object obj1,String obj2,Object obj3){");
        svCode.append("public void setPropertyValue(Object obj1,String obj2,Object obj3){");
        ivCode.append("public Object invoke(Object obj1,String obj2,Class[] obj3,Object[] obj4){");
        svCode.append(className).append(" w;").append(" try{w=($w)obj1;}catch(Exception e){e.printStackTrace();}");
        gvCode.append(className).append(" w;").append(" try{w=($w)obj1;}catch(Exception e){e.printStackTrace();}");
        ivCode.append(className).append(" w;").append(" try{w=($w)obj1;}catch(Exception e){e.printStackTrace();}");
        for(Field field:cls.getFields()){
            String fn=field.getName();
            if(Modifier.isStatic(field.getModifiers()))continue;
            gvCode.append("if(").append(fn).append(".equals(\"$2\")){").append("w.").append(fn).append("=$3;}");
            svCode.append("if(").append(fn).append(".equals(\"$2\")){").append("return w.").append(fn).append(";");
        }
        svCode.append("return null;}");
        for(Method method:cls.getMethods()){
            StringBuilder sb=new StringBuilder();
            String mn=method.getName();
            Class rt=method.getReturnType();
            Class<?>[] pts=method.getParameterTypes();
            ivCode.append("if(").append(mn).append(".equals(\"$2\")&&$3.length").append("==").append(pts.length).append("){");
            ivCode.append("boolean flag=true;");
            for(int i=0;i<pts.length;i++){
                if(i!=0){
                    sb.append(",");
                }
                sb.append("($3[").append(i).append("].getName()) ").append("$4[").append(i).append("]");
                ivCode.append("!if($3[i].getName().equals(\"").append(pts[i].getName()).append("\")){").append("flag=false;}");
            }
            ivCode.append("if(flag){");
            if(Void.TYPE.equals(rt)) {
                ivCode.append("return ");
//                ivCode.append("if(flag){return w.").append(mn).append("(").append(sb).append(")");
            }
            ivCode.append("w.").append(mn).append("(").append(sb).append(");}");
        }
        ivCode.append("return null;}");

        return null;
    }


    abstract  public String[] getMethodNames();
    abstract public boolean hasMethodName(String methodName);
    abstract  public Object getPropertyValue(Object instance,String parameterName);
    abstract  public void setPropertyValue(Object instance,String parameterName,Object pv);
    abstract public Object invoke(Object instance,String mn,Class[] classes,Object[] objs);
}
