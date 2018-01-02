package com.hatox.common.javassist;

public class BaseClass {
    public static String args(Class c,String s){
        if(c.isPrimitive()) {
            if (c == Boolean.TYPE) {
                return "((Boolean)" + s + ").booleanValue()";
            }
            if (c == Character.TYPE) {
                return "((Character)" + s + ").charValue()";
            }
            if (c == Float.TYPE) {
                return "((Float)" + s + ").floatValue()";
            }
            if (c == Double.TYPE) {
                return "((Double)" + s + ").doubleValue()";
            }
            if (c == Byte.TYPE) {
                return "((Byte)" + s + ").byteValue()";
            }
            if (c == Short.TYPE) {
                return "((Short)" + s + ").shortValue()";
            }
            if (c == Integer.TYPE) {
                return "((Integer)" + s + ").intValue()";
            }
            if (c == Long.TYPE) {
                return "((Long)" + s + ").longValue()";
            } else throw new RuntimeException("Unknown primitive type: " + c.getName());
        }
        return "("+getName(c)+")"+s;
    }
    public static String getName(Class c){
        StringBuilder sb=new StringBuilder();
        if(c.isArray()){
            do {
                sb.append("[]");
                c = c.getComponentType();
            }while(c.isArray());
        }
        return c.getName()+sb;
    }
    public static String arg(Class c){
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
}
