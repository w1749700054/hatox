package com.hatox.common.javassist;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

abstract public  class Wrap {
    private  final static AtomicLong WRAPPER_ATOM=new AtomicLong(0);
    public static Wrap getWrap(Class cls){
        while(ClassGenerator.isDynamicClass(cls))
            cls.getSuperclass();
        if(cls.equals(Object.class))return null;
        ClassLoader cl=cls.getClassLoader();
        String className=cls.getName();
        try {
            StringBuilder svCode=new StringBuilder();
            StringBuilder gvCode=new StringBuilder();
            StringBuilder ivCode=new StringBuilder();
            gvCode.append("public Object getPropertyValue(java.lang.Object instance,java.lang.String parameterName){ ");
            svCode.append("public void setPropertyValue(Object obj1,String obj2,Object obj3){");
            ivCode.append("public Object invoke(Object obj1,String obj2,Class[] obj3,Object[] obj4){");
            svCode.append(className).append(" w;").append(" try{ w =(").append(className).append(")$1;}catch(Exception e){throw new IllegalArgumentException(e); }");
            gvCode.append(className).append(" w;").append(" try{ w =(").append(className).append(")$1;}catch(Exception e){throw new IllegalArgumentException(e); }");
            ivCode.append(className).append(" w;").append(" try{ w =(").append(className).append(")$1;}catch(Exception e){throw new IllegalArgumentException(e);}");
            for(Field field:cls.getFields()){
                String fn=field.getName();
                Class fieldClass=field.getType();
                System.out.println(field.getModifiers());
                if(Modifier.isStatic(field.getModifiers()))continue;
//                if(fieldClass.is)
                svCode.append("if(").append(fn).append(".equals(\"$2\")){").append("w.").append(fn).append("=").append(args(fieldClass,"$3"));
                gvCode.append("if(").append(fn).append(".equals(\"$2\")){").append("return w.").append(fn).append(";");
            }
            List<String> methodList=new ArrayList(cls.getMethods().length);
            for(Method method:cls.getMethods()){
                methodList.add(method.getName());
                StringBuilder sb=new StringBuilder();
                String mn=method.getName();
                Class rt=method.getReturnType();
                Class<?>[] pts=method.getParameterTypes();
                if((mn.startsWith("get")&&mn.length()>3&&Character.isUpperCase(mn.charAt(3))
                        ||(mn.startsWith("is")&&mn.length()>2&&Character.isUpperCase(mn.charAt(2))))&&pts.length==0&&(!Void.TYPE.equals(rt))){
                    String ptName=null;
                    if(mn.startsWith("get")){
                        ptName=mn.substring(3,4).toLowerCase()+mn.substring(4,mn.length());
                    }
                    if(mn.startsWith("is")){
                        ptName=mn.substring(2,3).toLowerCase()+mn.substring(3,mn.length());
                    }
                    if(ptName!=null&&ptName.length()>0){
                        gvCode.append("if(\"").append(ptName).append("\".equals($2))").append("{return ($w)w.").append(mn).append("();}");
                    }
                }
                if((mn.startsWith("set")&&mn.length()>3&&Character.isUpperCase(mn.charAt(3)))&&pts.length==1&&(Void.TYPE.equals(rt))){
                    String ptName=mn.substring(3,4).toLowerCase()+mn.substring(4,mn.length());
                    svCode.append("if(\"").append(ptName).append("\".equals($2)){").append("w.").append(mn).append("(").append(args(pts[0],"$3")).append("); return;}");
                }
                ivCode.append("if(\"").append(mn).append("\".equals($2)&&$3.length").append("==").append(pts.length).append("){");
                ivCode.append("boolean flag=true;");
                for(int i=0;i<pts.length;i++){
                    if(i!=0){
                        sb.append(",");
                    }
                    sb.append(args(pts[i],"$4["+i+"]"));
//                    sb.append("(").append(pts[i].getName()).append(")").append("$4[").append(i).append("]");
                    ivCode.append("if(!$3[").append(i).append("].getName().equals(\"").append(args(pts[i])).append("\")){").append("flag=false;}");
                }
                ivCode.append("if(flag){");
                ivCode.append("Object ret=null;");

                if(!Void.TYPE.equals(rt)) {
                    ivCode.append("ret=($w)");
//                    ivCode.append("return ($w)");
//                    ivCode.append("if(flag){return w.").append(mn).append("(").append(sb).append(");");
                }
                ivCode.append("w.").append(mn).append("(").append(sb).append(");");
                ivCode.append("return ret").append(";}}");
            }
            svCode.append("throw new Exception(\"set\"+$2.substring(0, 1).toUpperCase() + $2.substring(1)+\" method is not found\");}");
            gvCode.append("throw new Exception(\"get||is\"+$2.substring(0, 1).toUpperCase() + $2.substring(1)+ \"method is not found\");}");
            ivCode.append("throw new Exception(\" invoke \"+$2+\" is not found\");}");
            ClassGenerator ccp=ClassGenerator.getInstance(cl);
            Long id=WRAPPER_ATOM.getAndIncrement();
            ccp.addMethod(gvCode.toString());
            ccp.addMethod(svCode.toString());
            ccp.addMethod(ivCode.toString());
            ccp.addMethod("public String[] getMethodNames(){return methods;}");
            ccp.addMethod("public boolean hasMethodName(String methodName){for(int i=0;i<methods.length;i++){if(methods[i].equals($1))return true;}return false;}");
            ccp.addField("public static String[] methods;");
            ccp.setClassName(Wrap.class.getName()+id);
            ccp.setSuperClassName(Wrap.class.getName());
            ccp.addDefaultConstrust();
            Class clazz=ccp.toClass();
            clazz.getField("methods").set(null,methodList.toArray(new String[0]));
            return (Wrap) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String args(Class c){
        if(c.isPrimitive()){
            if(c==Boolean.TYPE){
                return Boolean.class.getName();
            }
            if(c==Character.TYPE){
                return Character.class.getName();
            }
            if(c==Float.TYPE){
                return Float.class.getName();
            }
            if(c==Double.TYPE){
                return Double.class.getName();
            }
            if(c==Byte.TYPE){
                return Byte.class.getName();
            }
            if(c==Short.TYPE){
                return Short.class.getName();
            }
            if(c==Integer.TYPE){
                return Integer.class.getName();
            }
            if(c==Long.TYPE){
                return Long.class.getName();
            }
            else throw new RuntimeException("Unknown primitive type: " + c.getName());
        }
        return c.getName();
    }
    private static String args(Class c,String s){
        if(c.isPrimitive()){
            if(c==Boolean.TYPE){
                return "((Boolean)"+s+").booleanValue()";
            }
            if(c==Character.TYPE){
                return "((Character)"+s+").charValue()";
            }
            if(c==Float.TYPE){
                return "((Float)"+s+").floatValue()";
            }
            if(c==Double.TYPE){
                return "((Double)"+s+").doubleValue()";
            }
            if(c==Byte.TYPE){
                return "((Byte)"+s+").byteValue()";
            }
            if(c==Short.TYPE){
                return "((Short)"+s+").shortValue()";
            }
            if(c==Integer.TYPE){
                return "((Integer)"+s+").intValue()";
            }
            if(c==Long.TYPE){
                return "((Long)"+s+").longValue()";
            }
            else throw new RuntimeException("Unknown primitive type: " + c.getName());
        }
        return "("+getName(c)+")"+s;
//        return null;
    }
    private static String getName(Class c){
        StringBuilder sb=new StringBuilder();
        if(c.isArray()){
            do {
                sb.append("[]");
                c = c.getComponentType();
            }while(c.isArray());
        }
        return c.getName()+sb;
    }


    abstract  public String[] getMethodNames();
    abstract public boolean hasMethodName(String methodName);
    abstract  public Object getPropertyValue(Object instance,String parameterName);
    abstract  public void setPropertyValue(Object instance,String parameterName,Object pv);
    abstract public Object invoke(Object instance,String mn,Class[] classes,Object[] objs);
}
