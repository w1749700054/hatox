package com.hatox.common.spi;
@SPI
public interface ExtensionFactory {
    <T> T getExtension(Class<T> type, String name) throws IllegalAccessException;
}
