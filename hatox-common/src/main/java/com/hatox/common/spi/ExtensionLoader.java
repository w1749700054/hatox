package com.hatox.common.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader <T>{

    private static final String SERVICES_DIRECTORY = "META-INF/services/";

    private static final String HOTAX_DIRECTORY = "META-INF/hotax/";

    private static final String HOTAX_INTENEL= "dubbo/intenel/";

    private Class<?> clazz;

    private Class cacheAdativeClass;

    private Set<Class<?>> cacheWrapperSet;

    private final static Map<String,Class<?>> cacheMap=new ConcurrentHashMap<String, Class<?>>();


    private final static Map<Class,ExtensionLoader> EXTENSION_MAP=new HashMap<Class,ExtensionLoader>();

    private final ExtensionFactory optionfactory;

    private Set<Class<?>> classSet;

    private ExtensionLoader(Class clazz){
        this.clazz=clazz;
        optionfactory = (clazz == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
    }
    public static <T>ExtensionLoader<T> getExtensionLoader(Class<T> clazz){
        if(clazz==null){
            throw new IllegalArgumentException("getExtension class is null");
        }
        ExtensionLoader extensionLoader=EXTENSION_MAP.get(clazz);
        if (extensionLoader==null){
            extensionLoader=new ExtensionLoader(clazz);
        }
        if(extensionLoader==null){
            throw new IllegalArgumentException("getExtensionLoader is Error");
        }
        return extensionLoader;
    }
    public T getAdaptiveExtension(){
        Object instance = null;
        getExtensionClass();
        try {
            instance=cacheMap.get("spi").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) instance;
    }
    public <T> T getDefaultExtension() throws IllegalAccessException, InstantiationException {
        SPI classAnnotation=clazz.getAnnotation(SPI.class);
        if(classAnnotation!=null){
            String value=classAnnotation.value();
            if(value!=null&&value.trim().length()>0){
                return getExtension(value);
            }
        }
        return null;
    }
    public <T> T getExtension(String name) throws InstantiationException, IllegalAccessException {
        Class defaultClass=cacheMap.get(name);
        if(defaultClass==null){
            getExtensionClass();
            defaultClass=cacheMap.get(name);
            if(defaultClass!=null){
                return getInstance(defaultClass);
            }
        }
        return null;
    }

    private <T> T getInstance(Class defaultClass) throws IllegalAccessException, InstantiationException {
        Method[] methods=defaultClass.getMethods();
        for(Method method:methods){
            if(method.isAnnotationPresent(Adaptive.class)){
             
            }
        }
        return (T) defaultClass.newInstance();

    }
    private void getExtensionClass(){
        Map<String,Class<?>> classes=new HashMap<String, Class<?>>();
        loadFile(classes,HOTAX_INTENEL);
        cacheMap.putAll(classes);
    }

    private  void loadFile(Map<String, Class<?>> classes, String dir) {
        String filename=dir+clazz.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader=this.getClass().getClassLoader();
            if (classLoader!=null) {
                urls = classLoader.getResources(filename);
            }else{
                urls=ClassLoader.getSystemResources(filename);
            }
            if(urls!=null){
                while(urls.hasMoreElements()){
                    URL url=urls.nextElement();
                    BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
                    String line;
                    while((line=br.readLine())!=null){
                        if(!line.startsWith("#")){
                            if(line.contains("#")){
                                line=line.substring(0,line.indexOf("#"));
                            }
                            line=line.trim();
                            if(line.length()>0){
                                int index=line.indexOf("=");
                                String key = null;
                                if(index>0){key=line.substring(0,index).trim();}
                                line=line.substring(index+1).trim();
                                if(line.length()>0){
                                    try {
                                        Class realClass=Class.forName(line,true,classLoader);
                                        if(!clazz.isAssignableFrom(realClass)){
                                            throw new IllegalStateException(realClass+"is not interface of"+classes);
                                        }
                                        if(realClass.isAnnotationPresent(Adaptive.class)){
                                            if(cacheAdativeClass==null){
                                                cacheAdativeClass=realClass;
                                            }
                                            else if(cacheAdativeClass.equals(clazz)){
                                                throw  new IllegalStateException("has too more one adative class");
                                            }
                                        }else{
                                            try {
                                                realClass.getConstructor(clazz);
                                                Set<Class<?>> cacheWrapper=cacheWrapperSet;
                                                if(cacheWrapper==null){
                                                    cacheWrapperSet=new ConcurrentHashSet();
                                                    cacheWrapper=cacheWrapperSet;
                                                }
                                                cacheWrapper.add(realClass);
                                            } catch (NoSuchMethodException e) {
                                                try {
                                                    realClass.getConstructor();
                                                    if (key==null||key.length()<1){
                                                        key=realClass.getSimpleName();
                                                    }

                                                } catch (NoSuchMethodException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        }
                                        classes.put(key,realClass);
                                    } catch (ClassNotFoundException e) {

                                    }
                                }
                            }
                        }
                    }
                    br.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ClassLoader getClassLoad() {
        return ExtensionLoader.class.getClassLoader();
    }
}
