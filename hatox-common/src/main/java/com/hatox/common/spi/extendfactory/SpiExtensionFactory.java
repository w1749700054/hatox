package com.hatox.common.spi.extendfactory;


import com.hatox.common.spi.ExtensionFactory;
import com.hatox.common.spi.ExtensionLoader;
import com.hatox.common.spi.SPI;

public class SpiExtensionFactory implements ExtensionFactory {
    public <T> T getExtension(Class<T> type, String name) throws IllegalAccessException {
        if(type.isInterface()&&type.isAnnotationPresent(SPI.class)){
            ExtensionLoader loader=ExtensionLoader.getExtensionLoader(type);
            if (loader!=null){
                return (T) loader;
            }
        }
        return null;
    }
}
