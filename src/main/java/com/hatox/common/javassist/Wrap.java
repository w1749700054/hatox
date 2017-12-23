package com.hatox.common.javassist;

public abstract class Wrap {
    public static Wrap getWrap(Class mInterface){
        return null;
    }

    abstract  public String[] getMethodNames();
    abstract public boolean hasMethodName();
    abstract  public Object getPropertyValue(Object instance,String parameterName);
    abstract  public void setPropertyValue(Object instance,String parameterName,Object pv);
    abstract public Object invoke(Object instance,String mn,Class[] classes,Object[] objs);
}
